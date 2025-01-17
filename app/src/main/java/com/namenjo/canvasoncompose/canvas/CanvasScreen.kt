package com.namenjo.canvasoncompose.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import kotlin.math.abs

@Composable
fun CanvasScreen(
    modifier: Modifier = Modifier,
    state: CanvasState,
    onEvent: (CanvasEvent) -> Unit,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Canvas(
            modifier = modifier
                .weight(1f)
                .clipToBounds()
                .background(Color.LightGray)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            onEvent(CanvasEvent.OnPathStarts)
                        },
                        onDragEnd = {
                            onEvent(CanvasEvent.OnPathEnds)
                        },
                        onDrag = { change, _ ->
                            onEvent(CanvasEvent.OnDraw(change.position))
                        },
                        onDragCancel = {
                            onEvent(CanvasEvent.OnPathEnds)
                        }
                    )
                }
        ) {
            state.paths.fastForEach { pathData ->
                drawPath(
                    path = pathData.path,
                    color = pathData.color
                )
            }
            state.currentPath?.let { pathData ->
                drawPath(
                    path = pathData.path,
                    color = pathData.color
                )
            }

        }
        // Clear
        Button(
            onClick = { onEvent(CanvasEvent.OnClearCanvas) }
        ) {
            Text(text = "Clear", fontSize = 18.sp)
        }
    }
}

private fun DrawScope.drawPath(
    path: List<Offset>,
    color: Color,
    thickness: Float = 10f,
) {
    val smoothedPath = Path().apply {
        if (path.isNotEmpty()) {
            moveTo(path.first().x, path.first().y)
            val smoothness = 5
            for (i in 1..path.lastIndex) {
                val from = path[i - 1]
                val to = path[i]
                val dx = abs(from.x - to.x)
                val dy = abs(from.y - to.y)
                if (dx >= smoothness || dy >= smoothness) {
                    quadraticTo(
                        x1 = (from.x + to.x) / 2f,
                        y1 = (from.y + to.y) / 2f,
                        x2 = to.x,
                        y2 = to.y
                    )
                }
            }
        }
    }
    drawPath(
        path = smoothedPath,
        color = color,
        style = Stroke(
            width = thickness,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}
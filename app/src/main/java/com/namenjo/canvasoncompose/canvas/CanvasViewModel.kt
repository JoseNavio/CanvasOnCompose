package com.namenjo.canvasoncompose.canvas

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CanvasState(
    val selectedColor: Color = Color.Black,
    val currentPath: PathData? = null,
    val paths: List<PathData> = emptyList(),
)

data class PathData(
    val id: String,
    val color: Color,
    val path: List<Offset>,
)

sealed interface CanvasEvent {
    data object OnPathStarts : CanvasEvent
    data object OnPathEnds : CanvasEvent
    data object OnClearCanvas : CanvasEvent
    data class OnDraw(val offset: Offset) : CanvasEvent
    data class OnColorSelected(val selectedColor: Color) : CanvasEvent
}

val colors = listOf(
    Color.Black,
    Color.Blue,
    Color.Red
)

class CanvasViewModel : ViewModel() {

    private val _state = MutableStateFlow(CanvasState())
    val state = _state.asStateFlow()

    fun onEvent(event: CanvasEvent) {

        when (event) {

            is CanvasEvent.OnPathStarts -> {
                Log.d("Navio_Draw","Starts")
                onPathStarts()
            }

            is CanvasEvent.OnPathEnds -> {
                Log.d("Navio_Draw","Ends")
                onPathEnds()
            }

            is CanvasEvent.OnClearCanvas -> {
                onClearCanvas()
            }

            is CanvasEvent.OnColorSelected -> {
                onColorSelected(event.selectedColor)
            }

            is CanvasEvent.OnDraw -> {
                Log.d("Navio_Draw","Draws")
                onDraw(event.offset)
            }

        }

    }

    private fun onDraw(offset: Offset) {
        // In case there is not a selected path returns
        _state.update { state ->
            state.copy(
                currentPath = state.currentPath?.let { currentPath ->
                    currentPath.copy(
                        path = currentPath.path + offset
                    )
                }
            )
        }
    }

    private fun onColorSelected(selectedColor: Color) {
        _state.update { it.copy(selectedColor = selectedColor) }
    }

    private fun onClearCanvas() {
        _state.update { CanvasState() }
    }

    private fun onPathEnds() {

        val newPath = _state.value.currentPath ?: return

        _state.update { state ->
            state.copy(
                currentPath = null,
                paths = state.paths + newPath
            )
        }
    }

    private fun onPathStarts() {
        _state.update { state ->
            state.copy(
                currentPath = PathData(
                    id = System.currentTimeMillis().toString(),
                    color = state.selectedColor,
                    path = emptyList()
                )
            )
        }
    }
}
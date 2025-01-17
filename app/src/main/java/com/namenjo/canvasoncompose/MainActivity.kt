package com.namenjo.canvasoncompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.namenjo.canvasoncompose.canvas.CanvasScreen
import com.namenjo.canvasoncompose.canvas.CanvasState
import com.namenjo.canvasoncompose.canvas.CanvasViewModel
import com.namenjo.canvasoncompose.ui.theme.CanvasOnComposeTheme

class MainActivity : ComponentActivity() {

    private val canvasViewModel by viewModels<CanvasViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            CanvasOnComposeTheme {

                Scaffold(
                    contentWindowInsets = WindowInsets.systemBars
                ) { innerPadding ->

                    val canvasState by canvasViewModel.state.collectAsState()

                        CanvasScreen(
                            modifier = Modifier.fillMaxWidth().padding(innerPadding),
                            state = canvasState,
                            onEvent = canvasViewModel::onEvent
                        )
                }
            }
        }
    }
}

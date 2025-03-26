package com.example.sigtrack_call

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import org.webrtc.SurfaceViewRenderer

// CallAnsweredScreen.kt
@Composable
fun CallAnsweredScreen(navController: NavController, webRTCManager: WebRTCManager) {
    val isLocalVideoSmall = remember { mutableStateOf(true) }
    val pipOffset = remember { mutableStateOf(Offset(50f, 100f)) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFEFEFEF))) {
        if (isLocalVideoSmall.value) {
            RemoteVideoPreview(webRTCManager.remoteView,
                Modifier.fillMaxSize())
        } else {
            LocalVideoPreview(webRTCManager.localView, Modifier.fillMaxSize())
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(pipOffset.value.x.toInt(), pipOffset.value.y.toInt()) }
                .size(150.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        pipOffset.value = pipOffset.value.copy(
                            x = (pipOffset.value.x + dragAmount.x).coerceIn(0f, 1000f),
                            y = (pipOffset.value.y + dragAmount.y).coerceIn(0f, 2000f)
                        )
                    }
                }
                .clickable { isLocalVideoSmall.value = !isLocalVideoSmall.value },
            contentAlignment = Alignment.Center
        ) {
            if (isLocalVideoSmall.value) {
                LocalVideoPreview(webRTCManager.localView, Modifier.fillMaxSize())
            } else {
                RemoteVideoPreview(webRTCManager.remoteView, Modifier.fillMaxSize())
            }
        }

        Column(
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CameraToggleButton { webRTCManager.toggleCamera() }
            MuteButton { webRTCManager.toggleMute() }
            EndCall(navController)
        }
    }
}

@Composable
fun LocalVideoPreview(surfaceViewRenderer: SurfaceViewRenderer, modifier: Modifier) {
    AndroidView(
        factory = { context ->
            surfaceViewRenderer.apply {
                setMirror(true) // Mirror for local video
                setZOrderMediaOverlay(true)
            }
        },
        modifier = modifier
    )
}

@Composable
fun RemoteVideoPreview(surfaceViewRenderer: SurfaceViewRenderer, modifier: Modifier) {
    AndroidView(
        factory = { context ->
            surfaceViewRenderer.apply {
                setMirror(false) // No mirror for remote video
                setZOrderMediaOverlay(false)
            }
        },
        modifier = modifier
    )
}

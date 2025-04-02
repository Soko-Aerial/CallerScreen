package com.example.sigtrack_call

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.sigtrack_calll.R
import org.webrtc.SurfaceViewRenderer

@Composable
fun CallAnsweredScreen(navController: NavController, webRTCManager: WebRTCManager) {
    RemoteVideoPreview(webRTCManager.remoteView, Modifier.fillMaxSize())
    LocalVideoPreview(webRTCManager.localView, Modifier.fillMaxSize())
    val isLocalVideoSmall = remember { mutableStateOf(true) }
    val pipOffset = remember { mutableStateOf(Offset(50f, 100f)) }
    RequestPermissions()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Large Video Preview (Toggles between Local & Remote)
        if (isLocalVideoSmall.value) {
            RemoteVideoPreview(webRTCManager.remoteView, Modifier.fillMaxSize())
        } else {
            LocalVideoPreview(webRTCManager.localView, Modifier.fillMaxSize())
        }

        // Draggable PiP (Picture-in-Picture) Local Preview
        Box(
            modifier = Modifier
                .offset { IntOffset(pipOffset.value.x.toInt(), pipOffset.value.y.toInt()) }
                .size(140.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(2.dp, Color.White, RoundedCornerShape(12.dp))
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

        // Buttons UI
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(12.dp)
            ) {
                CameraToggleButton { webRTCManager.toggleCamera() }
                MuteButton { webRTCManager.toggleMute() }
                EndCall(navController)
            }
        }
    }
}

@Composable
fun LocalVideoPreview(surfaceViewRenderer: SurfaceViewRenderer, modifier: Modifier) {
    AndroidView(
        factory = { context ->
            surfaceViewRenderer.apply {
                setMirror(true)
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
                setMirror(false)
                setZOrderMediaOverlay(false)
            }
        },
        modifier = modifier
    )
}

@Composable
fun CameraToggleButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.camera),
            contentDescription = "Toggle Camera",
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun MuteButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Mute",
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun EndCall(navController: NavController) {
    IconButton(
        onClick = {
            navController.navigate("declineScreen") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        },
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(Color.Red)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.call2),
            contentDescription = "End Call",
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}

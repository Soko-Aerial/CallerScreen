package com.example.sigtrack_call


import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.sigtrack_calll.R


@Composable
fun AnswerScreen(navController: NavController) {
    val cameraSelectorState = remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    val isLocalVideoSmall = remember { mutableStateOf(true) } // Track which preview is small
    val pipOffset = remember { mutableStateOf(Offset(50f, 100f)) } // PiP position

    RequestPermissions()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFEFEFEF))) {
        // Large Preview (Can be either local or remote video)
        if (isLocalVideoSmall.value) {
            RemoteVideoPreview(modifier = Modifier.fillMaxSize())
        } else {
            CameraPreview(LocalLifecycleOwner.current, cameraSelectorState, Modifier.fillMaxSize())
        }

        // Draggable PiP Local Preview
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
                CameraPreview(
                    LocalLifecycleOwner.current,
                    cameraSelectorState,
                    Modifier.fillMaxSize()
                )
            } else {
                RemoteVideoPreview(modifier = Modifier.fillMaxSize())
            }
        }

        // Buttons
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CameraToggleButton(cameraSelectorState)
            EndCall(navController)
        }
    }
}

@Composable
fun CameraPreview(
    lifecycleOwner: LifecycleOwner,
    cameraSelectorState: MutableState<CameraSelector>,
    modifier: Modifier
) {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    LaunchedEffect(cameraSelectorState.value) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.surfaceProvider = previewView.surfaceProvider
        }
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelectorState.value, preview)
        } catch (e: Exception) {
            Log.e("CameraPreview", "Use case binding failed", e)
        }
    }
    AndroidView({ previewView }, modifier = modifier)
}

@Composable
fun RemoteVideoPreview(modifier: Modifier) {
    Box(modifier = modifier
        .background(Color.Gray),
        contentAlignment = Alignment.Center) {
        Text("Remote Video", color = Color.White)
    }
}

@Composable
fun CircularButton(icon: Int, backgroundColor: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun CameraToggleButton(cameraSelectorState: MutableState<CameraSelector>) {
    CircularButton(
        icon = R.drawable.camera,
        backgroundColor = Color.LightGray,
        onClick = {
            cameraSelectorState.value =
                if (cameraSelectorState.value == CameraSelector.DEFAULT_BACK_CAMERA)
                    CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
        }
    )
}

@Composable
fun EndCall(navController: NavController) {
    CircularButton(icon = R.drawable.call2,
        backgroundColor = Color.LightGray) {
        navController.navigate("declineScreen")
    }
}


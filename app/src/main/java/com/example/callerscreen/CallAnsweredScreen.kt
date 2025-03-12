package com.example.callerscreen

import android.Manifest
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
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun AnswerScreen(navController: NavController) {
    val cameraSelectorState = remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }

    RequestPermissions()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
    ) {
        // Camera Preview
        CameraPreview(lifecycleOwner = LocalLifecycleOwner.current, cameraSelectorState)

        // Buttons
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CameraToggleButton(cameraSelectorState)
            EndCall(navController = navController)
        }
    }
}

@Composable
fun CameraPreview(lifecycleOwner: LifecycleOwner, cameraSelectorState: MutableState<CameraSelector>) {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    LaunchedEffect(cameraSelectorState.value) {
        try {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelectorState.value, preview)
        } catch (e: Exception) {
            Log.e("CameraPreview", "Camera binding failed", e)
        }
    }

    AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
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
                    CameraSelector.DEFAULT_FRONT_CAMERA
                else
                    CameraSelector.DEFAULT_BACK_CAMERA
        }
    )
}

@Composable
fun EndCall(navController: NavController) {
    CircularButton(
        icon = R.drawable.call2,
        backgroundColor = Color.LightGray,
        onClick = { navController.navigate("declineScreen") }
    )
}

@Composable
fun RequestPermissions() {
    val context = LocalContext.current
    val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsGranted ->
        if (!permissionsGranted.values.all { it }) {
            Toast.makeText(context, "Permissions required!", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(permissions)
    }
}

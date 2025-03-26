package com.example.sigtrack_call

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sigtrack_calll.R

// Buttons.kt
@Composable
fun CircularButton(icon: Int, backgroundColor: androidx.compose.ui.graphics.Color, onClick: () -> Unit) {
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
fun CameraToggleButton(onClick: () -> Unit) {
    CircularButton(icon = R.drawable.camera, backgroundColor = androidx.compose.ui.graphics.Color.Gray, onClick = onClick)
}

@Composable
fun MuteButton(onClick: () -> Unit) {
    CircularButton(icon = R.drawable.ic_launcher_foreground, backgroundColor = androidx.compose.ui.graphics.Color.DarkGray, onClick = onClick)
}

@Composable
fun EndCall(navController: NavController) {
    CircularButton(icon = R.drawable.call2, backgroundColor = androidx.compose.ui.graphics.Color.LightGray) {
        navController.navigate("declineScreen") {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }
}

package com.example.callerscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun AnswerScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
    ) {
        // Title Text
        Text(
            "ALPHA KILO",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9A9A00),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
        )

        // Corner lines
        CornerLines()

        // Buttons on the right
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CameraButton()

            EndCall()

        }

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
fun CornerLines() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val lineLength = 50f
        val strokeWidth = 4f
        val color = Color(0xFF9A9A00)

        // Top-left
        drawLine(
            color = color,
            start = Offset(0f, lineLength),
            end = Offset(lineLength, 0f),
            strokeWidth = strokeWidth
        )

        // Top-right
        drawLine(
            color = color,
            start = Offset(size.width - lineLength, 0f),
            end = Offset(size.width, lineLength),
            strokeWidth = strokeWidth
        )

        // Bottom-left
        drawLine(
            color = color,
            start = Offset(0f, size.height - lineLength),
            end = Offset(lineLength, size.height),
            strokeWidth = strokeWidth
        )

        // Bottom-right
        drawLine(
            color = color,
            start = Offset(size.width - lineLength, size.height),
            end = Offset(size.width, size.height - lineLength),
            strokeWidth = strokeWidth
        )
    }
}

@Composable
fun CameraButton(modifier: Modifier = Modifier) {
    CircularButton(
        icon = R.drawable.camera,
        backgroundColor = Color.LightGray,
        onClick = { /* Sync action */ })
}

@Composable
fun EndCall(modifier: Modifier = Modifier) {
    CircularButton(
        icon = R.drawable.call2,
        backgroundColor = Color.LightGray,
        onClick = { /* End call action */ })
}



package com.example.callerscreen

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.callerscreen.ui.theme.CallerScreenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CallerScreenTheme {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                com.example.sigtrack_calll.Navigation()
            }
        }
    }
}


@Preview
@Composable
private fun CallerScreen() {
    com.example.sigtrack_calll.Navigation()
}


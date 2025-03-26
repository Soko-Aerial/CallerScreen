package com.example.sigtrack_call

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController

// MainActivity.kt
class MainActivity : ComponentActivity() {
    private lateinit var webRTCManager: WebRTCManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webRTCManager = WebRTCManager(this)
        webRTCManager.setupLocalTracks()

        setContent {
            val navController = rememberNavController()
            CallAnsweredScreen(navController, webRTCManager)
        }
    }
}

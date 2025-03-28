package com.example.sigtrack_call

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    private lateinit var webRTCManager: WebRTCManager
    private lateinit var signalingClient: SignalingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webRTCManager = WebRTCManager(this, signalingClient)

        signalingClient = SignalingClient(
            serverUrl = "wss://your-signaling-server.com",
            webRTCManager = webRTCManager,
            onIncomingCall = { callerId ->
                // Handle an incoming call (navigate to the incoming call screen)
                println("Incoming call from $callerId")
            },
            onCallDeclined = {
                // Handle call decline logic
                println("Call was declined.")
            }
        )

        // Assign the signaling client to WebRTCManager
        webRTCManager.signalingClient = signalingClient

        setContent {
            Navigation(webRTCManager, signalingClient)
        }
    }
}

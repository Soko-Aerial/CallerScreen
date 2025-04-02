package com.example.callerscreen

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.callerscreen.ui.theme.CallerScreenTheme
import com.example.sigtrack_call.Navigation
import com.example.sigtrack_call.SignalingClient
import com.example.sigtrack_call.WebRTCManager

class MainActivity : ComponentActivity() {
    private lateinit var webRTCManager: WebRTCManager
    private lateinit var signalingClient: SignalingClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CallerScreenTheme {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

                // Initialize WebRTCManager first
                webRTCManager = WebRTCManager(this, signalingClient)


                signalingClient = SignalingClient(
                    serverUrl = "wss://your-signaling-server.com",
                    webRTCManager = webRTCManager,
                    onIncomingCall = { callerId ->  // Passes callerId as expected
                        println("Incoming call from $callerId")
                        // TODO: Navigate to IncomingCallScreen and pass the callerId if needed
                    },
                    onCallDeclined = {
                        println("Call was declined.")
                        // TODO: Handle navigation after decline if necessary
                    }
                )

                // Assign the signaling client to WebRTCManager
                webRTCManager.signalingClient = signalingClient

                setContent {
                    Navigation(webRTCManager, signalingClient)
                }
            }
        }
    }
}


@Preview
@Composable
private fun CallerScreen() {
//    com.example.sigtrack_call.Navigation()
}


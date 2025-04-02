//package com.example.sigtrack_call
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//
//class MainActivity : ComponentActivity() {
//    private lateinit var webRTCManager: WebRTCManager
//    private lateinit var signalingClient: SignalingClient
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Initialize WebRTCManager first
//        webRTCManager = WebRTCManager(this, signalingClient)
//
//        // Now initialize SignalingClient with proper callbacks
//        signalingClient = SignalingClient(
//            serverUrl = "wss://your-signaling-server.com",
//            webRTCManager = webRTCManager,
//            onIncomingCall = { callerId ->  // Passes callerId as expected
//                println("Incoming call from $callerId")
//                // TODO: Navigate to IncomingCallScreen and pass the callerId if needed
//            },
//            onCallDeclined = {
//                println("Call was declined.")
//                // TODO: Handle navigation after decline if necessary
//            }
//        )
//
//        // Assign the signaling client to WebRTCManager
//        webRTCManager.signalingClient = signalingClient
//
//        setContent {
//            Navigation(webRTCManager, signalingClient)
//        }
//    }
//}

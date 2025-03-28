package com.example.sigtrack_call

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(
    webRTCManager: WebRTCManager,
    signalingClient: SignalingClient
) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "incomingCallScreen") {
        composable("incomingCallScreen") {
            IncomingCallScreen(
                navController, signalingClient,
                webRTCManager = webRTCManager
            )
        }
        composable("answeredScreen") {
            CallAnsweredScreen(navController, webRTCManager)
        }
        composable("declineScreen") {
            DeclineScreen(navController)
        }
    }
}

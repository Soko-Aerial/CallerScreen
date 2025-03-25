package com.example.sigtrack_call

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "callScreen") {
        composable("callScreen") { CallScreen(navController) }
        composable("declineScreen") { DeclineScreen(navController) }
        composable("answeredScreen") { AnswerScreen(navController) }
    }
}
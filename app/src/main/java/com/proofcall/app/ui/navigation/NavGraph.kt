package com.proofcall.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proofcall.app.ui.screens.HomeScreen
import com.proofcall.app.ui.screens.RecordingScreen
import com.proofcall.app.ui.screens.CallDetailScreen
import com.proofcall.app.ui.screens.PaywallScreen

object Routes {
    const val HOME = "home"
    const val RECORD = "record"
    const val DETAIL = "detail/{callId}"
    const val PAYWALL = "paywall"
}

@Composable
fun ProofNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) { HomeScreen(navController) }
        composable(Routes.RECORD) { RecordingScreen(navController) }
        composable(Routes.PAYWALL) { PaywallScreen(navController) }
        composable(Routes.DETAIL) { backStackEntry ->
            val callId = backStackEntry.arguments?.getString("callId") ?: ""
            CallDetailScreen(navController, callId)
        }
    }
}

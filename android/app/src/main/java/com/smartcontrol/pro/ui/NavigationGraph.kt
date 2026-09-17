package com.smartcontrol.pro.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smartcontrol.pro.ui.profile.AppRole

object Routes { const val HOME = "home"; const val DEVICE = "device/{deviceId}"; const val CONSENT = "consent"; const val ALERTS = "alerts"; const val PROFILE = "profile"; const val ADMIN = "admin" }

@Composable
fun SmartControlRoot(currentRole: AppRole, navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) { SmartControlApp(currentRole, onAdminPanel = { navController.navigate(Routes.ADMIN) }) }
        composable(Routes.DEVICE) { DeviceDetailScreen(onBack = { navController.popBackStack() }) }
        composable(Routes.CONSENT) { ConsentScreenStandalone() }
        composable(Routes.ALERTS) { AlertsScreenStandalone() }
        composable(Routes.PROFILE) { ProfileScreenStandalone(currentRole, onAdminPanel = { navController.navigate(Routes.ADMIN) }) }
        composable(Routes.ADMIN) { AdminPanelScreen(onBack = { navController.popBackStack() }) }
    }
}

package com.smartcontrol.owner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.smartcontrol.owner.ui.adddevice.AddDeviceScreen
import com.smartcontrol.owner.ui.auth.LoginScreen
import com.smartcontrol.owner.ui.auth.RegisterScreen
import com.smartcontrol.owner.ui.consent.ConsentCenterScreen
import com.smartcontrol.owner.ui.dashboard.DashboardScreen
import com.smartcontrol.owner.ui.devicedetail.DeviceDetailScreen
import com.smartcontrol.owner.ui.invite.InviteScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    // For demo, start at login. In production, check token first.
    var startDestination by remember { mutableStateOf(Routes.LOGIN) }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ============================================
        // AUTH
        // ============================================
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // ============================================
        // DASHBOARD
        // ============================================
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onAddDevice = { navController.navigate(Routes.ADD_DEVICE) },
                onDeviceClick = { deviceId ->
                    navController.navigate(Routes.deviceDetail(deviceId))
                },
                onConsentClick = { deviceId ->
                    navController.navigate(Routes.consentCenter(deviceId))
                },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                }
            )
        }

        // ============================================
        // ADD DEVICE
        // ============================================
        composable(Routes.ADD_DEVICE) {
            AddDeviceScreen(
                onBack = { navController.popBackStack() },
                onGenerateInvite = { deviceType, permissions, expiryHours ->
                    navController.navigate(Routes.INVITE)
                }
            )
        }

        // ============================================
        // INVITE
        // ============================================
        composable(Routes.INVITE) {
            InviteScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // ============================================
        // DEVICE DETAIL
        // ============================================
        composable(
            route = Routes.DEVICE_DETAIL,
            arguments = listOf(navArgument("deviceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getString("deviceId") ?: ""
            DeviceDetailScreen(
                deviceId = deviceId,
                onBack = { navController.popBackStack() },
                onConsentClick = { navController.navigate(Routes.consentCenter(deviceId)) }
            )
        }

        // ============================================
        // CONSENT CENTER
        // ============================================
        composable(
            route = Routes.CONSENT_CENTER,
            arguments = listOf(navArgument("deviceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getString("deviceId") ?: ""
            ConsentCenterScreen(
                deviceId = deviceId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
package com.smartcontrol.pro.ui.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column

enum class AppRole { SUPER_ADMIN, OWNER, ADMIN, MODERATOR, SUPPORT, FINANCE_ADMIN, LEGAL_ADMIN, USER }

fun canAccessAdminPanel(role: AppRole): Boolean = role == AppRole.SUPER_ADMIN || role == AppRole.OWNER

@Composable
fun ProfileScreen(currentRole: AppRole, openAdminPanel: () -> Unit) {
    Column {
        // Other profile and privacy settings go here.
        // Non-owner users receive no Admin Panel UI at all.
        if (canAccessAdminPanel(currentRole)) {
            SettingsItem(
                title = "Admin Panel",
                icon = Icons.Default.Lock,
                onClick = openAdminPanel,
            )
        }
    }
}

// Replace with the project's shared Material 3 settings component.
@Composable
private fun SettingsItem(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    androidx.compose.material3.TextButton(onClick = onClick) {
        androidx.compose.material3.Icon(icon, contentDescription = null)
        androidx.compose.material3.Text(title)
    }
}

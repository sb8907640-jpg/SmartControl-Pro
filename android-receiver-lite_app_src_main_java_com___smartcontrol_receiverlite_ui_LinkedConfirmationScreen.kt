package com.smartcontrol.receiverlite.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LinkedConfirmationScreen(
    grantedPermissions: List<PermissionItem>,
    onStopAll: () -> Unit,
    onManagePermissions: () -> Unit,
    onUnlink: () -> Unit,
    onRevokeAll: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "✅",
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = "Device Linked!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Aapka device successfully jud gaya hai.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Active permissions
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Active Permissions:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (grantedPermissions.isEmpty()) {
                    Text(
                        text = "Koi permission allow nahi ki gayi",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    grantedPermissions.forEach { permission ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(permission.icon)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = permission.title,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "✅ ON",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ⚠️ CRITICAL: BIG RED STOP BUTTON — Always visible
        Button(
            onClick = onStopAll,
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD32F2F)
            )
        ) {
            Text(
                text = "🛑 STOP ALL",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Yeh button aapko har screen par dikhega. Dabaane par turant sab band ho jayega.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Manage options
        OutlinedButton(
            onClick = onManagePermissions,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Manage Permissions")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onRevokeAll,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Revoke All Permissions")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onUnlink,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Unlink Device")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
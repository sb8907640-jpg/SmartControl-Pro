package com.smartcontrol.receiverlite.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(
    ownerName: String,
    permissions: List<String>,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    onViewPermissions: () -> Unit,
    onLearnMore: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "SmartControl Pro",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Receiver Lite",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // CRITICAL: Show who is inviting
        Text(
            text = "$ownerName ne aapko invite kiya hai",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // CRITICAL: Show EXACTLY what will be shared
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Jo share hoga:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                permissions.forEach { permission ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("✓ ", color = MaterialTheme.colorScheme.primary)
                        Text(permission, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // CRITICAL: Show user rights
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "⚠ Aapke Adhikaar:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("• Aap kabhi bhi STOP daba sakte hain")
                Text("• Aap kabhi bhi uninstall kar sakte hain")
                Text("• Aap kabhi bhi consent revoke kar sakte hain")
                Text("• Bina aapki permission kuch nahi hoga")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Primary action
        Button(
            onClick = onAccept,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Accept & Install", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onDecline,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Decline")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                onClick = onViewPermissions,
                modifier = Modifier.weight(1f)
            ) {
                Text("View Permissions")
            }
            TextButton(
                onClick = onLearnMore,
                modifier = Modifier.weight(1f)
            ) {
                Text("Learn More")
            }
        }
    }
}
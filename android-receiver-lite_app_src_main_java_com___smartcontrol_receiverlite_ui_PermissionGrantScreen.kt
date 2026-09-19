package com.smartcontrol.receiverlite.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class PermissionItem(
    val id: String,
    val title: String,
    val description: String,
    val icon: String
)

@Composable
fun PermissionGrantScreen(
    permissions: List<PermissionItem>,
    onContinue: (List<String>) -> Unit,
    onSaveDraft: () -> Unit
) {
    // Track which permissions user has toggled ON
    val grantedState = remember {
        mutableStateMapOf<String, Boolean>().apply {
            permissions.forEach { put(it.id, false) }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Permission Grant",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Aap kya allow karna chahte hain? Har permission alag se control karein.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Quick action buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    permissions.forEach { grantedState[it.id] = true }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("ALLOW ALL", style = MaterialTheme.typography.labelMedium)
            }

            OutlinedButton(
                onClick = {
                    // Custom — user toggles individually
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("CUSTOM", style = MaterialTheme.typography.labelMedium)
            }

            OutlinedButton(
                onClick = {
                    permissions.forEach { grantedState[it.id] = false }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("DENY ALL", style = MaterialTheme.typography.labelMedium)
            }
        }

        // Permission cards
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(permissions) { permission ->
                PermissionCard(
                    permission = permission,
                    isGranted = grantedState[permission.id] == true,
                    onToggle = { granted ->
                        grantedState[permission.id] = granted
                    }
                )
            }
        }

        // Footer
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Yeh permission aapki consent se chalegi. Aap kabhi bhi revoke kar sakte hain. Bina consent kuch nahi hoga.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val granted = grantedState.filter { it.value }.keys.toList()
                        onContinue(granted)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        "Continue → Link Device",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onSaveDraft,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Draft")
                }
            }
        }
    }
}

@Composable
fun PermissionCard(
    permission: PermissionItem,
    isGranted: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isGranted) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = permission.icon,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = permission.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = permission.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = isGranted,
                    onCheckedChange = onToggle
                )
            }

            if (isGranted) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✅ Aapne is permission ko allow kiya hai. Aap kabhi bhi revoke kar sakte hain.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
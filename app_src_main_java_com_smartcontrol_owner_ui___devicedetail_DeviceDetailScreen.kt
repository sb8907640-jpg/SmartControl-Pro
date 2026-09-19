package com.smartcontrol.owner.ui.devicedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartcontrol.owner.ui.theme.OfflineRed
import com.smartcontrol.owner.ui.theme.OnlineGreen
import com.smartcontrol.owner.util.Constants
import com.smartcontrol.owner.util.DateTimeUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDetailScreen(
    deviceId: String,
    onBack: () -> Unit,
    onConsentClick: () -> Unit,
    viewModel: DeviceDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showUnlinkDialog by remember { mutableStateOf(false) }
    var unlinkReason by remember { mutableStateOf("") }

    LaunchedEffect(deviceId) {
        viewModel.loadDevice(deviceId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.device?.deviceName ?: "Device Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showUnlinkDialog = true }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Unlink",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        val device = uiState.device ?: return@Scaffold

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Status header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(if (device.isOnline) OnlineGreen else OfflineRed)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (device.isOnline) "🟢 Online" else "🔴 Offline",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Last seen: ${DateTimeUtil.timeAgo(DateTimeUtil.parseIso(device.lastSeenAt))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Linked: ${DateTimeUtil.formatDateOnly(DateTimeUtil.parseIso(device.linkedAt))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Type: ${device.deviceType ?: "—"} • ${device.osVersion ?: "—"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Quick actions grid
            Text(
                "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            val actions = listOf(
                "📍" to "Location",
                "📷" to "Camera",
                "🎤" to "Mic",
                "🖥️" to "Screen",
                "👆" to "Touch",
                "⌨️" to "Keyboard",
                "📁" to "Files",
                "📋" to "Clipboard",
                "📦" to "App Install",
                "🆘" to "SOS",
                "🖼️" to "Gallery",
                "📊" to "History"
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(220.dp)
            ) {
                items(actions) { (icon, label) ->
                    ActionTile(icon = icon, label = label) { }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Active consents summary
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Active Permissions",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        TextButton(onClick = onConsentClick) {
                            Text("Manage")
                        }
                    }

                    val activePermissions = uiState.consents.filter {
                        it.granted && it.revokedAt == null
                    }

                    if (activePermissions.isEmpty()) {
                        Text(
                            "Koi permission active nahi hai",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        activePermissions.take(6).forEach { consent ->
                            val permInfo = Constants.ALL_PERMISSIONS.find { it.id == consent.featureName }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(permInfo?.icon ?: "✅")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    permInfo?.title ?: consent.featureName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    "✅ ON",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnlineGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        if (activePermissions.size > 6) {
                            Text(
                                "+ ${activePermissions.size - 6} more",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Revoke all button
            OutlinedButton(
                onClick = { viewModel.revokeAll(deviceId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Revoke All Permissions")
            }
        }
    }

    // Unlink dialog
    if (showUnlinkDialog) {
        AlertDialog(
            onDismissRequest = { showUnlinkDialog = false },
            title = { Text("Unlink Device?") },
            text = {
                Column {
                    Text("Device hat jayega, saara data delete ho jayega. Ye action undo nahi ho sakta.")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = unlinkReason,
                        onValueChange = { unlinkReason = it },
                        label = { Text("Reason (optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.unlinkDevice(deviceId, unlinkReason.ifBlank { null }) {
                            showUnlinkDialog = false
                            onBack()
                        }
                    }
                ) {
                    Text("Unlink", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showUnlinkDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ActionTile(icon: String, label: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1
            )
        }
    }
}
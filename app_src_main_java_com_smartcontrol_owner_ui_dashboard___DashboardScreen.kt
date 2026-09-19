package com.smartcontrol.owner.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartcontrol.owner.data.model.Device
import com.smartcontrol.owner.ui.theme.OfflineRed
import com.smartcontrol.owner.ui.theme.OnlineGreen
import com.smartcontrol.owner.util.DateTimeUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onAddDevice: () -> Unit,
    onDeviceClick: (String) -> Unit,
    onConsentClick: (String) -> Unit,
    onLogout: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SmartControl Pro", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { /* Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { /* Notifications */ }) {
                        BadgedBox(
                            badge = {
                                if (uiState.totalAlerts > 0) {
                                    Badge { Text(uiState.totalAlerts.toString()) }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Alerts")
                        }
                    }
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddDevice,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Device") },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Summary cards
            LazyRow(
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    SummaryCard(
                        title = "Total",
                        value = uiState.totalDevices.toString(),
                        icon = "📱",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                item {
                    SummaryCard(
                        title = "Online",
                        value = uiState.onlineDevices.toString(),
                        icon = "🟢",
                        color = OnlineGreen
                    )
                }
                item {
                    SummaryCard(
                        title = "Offline",
                        value = (uiState.totalDevices - uiState.onlineDevices).toString(),
                        icon = "🔴",
                        color = OfflineRed
                    )
                }
                item {
                    SummaryCard(
                        title = "Alerts",
                        value = uiState.totalAlerts.toString(),
                        icon = "🔔",
                        color = Color(0xFFF59E0B)
                    )
                }
            }

            // Quick actions
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { QuickAction("📍", "Location") {} }
                item { QuickAction("🔒", "Lock") {} }
                item { QuickAction("👆", "Touch") {} }
                item { QuickAction("⌨️", "Keyboard") {} }
                item { QuickAction("🖥️", "Record") {} }
                item { QuickAction("📁", "Files") {} }
                item { QuickAction("🆘", "SOS") {} }
                item { QuickAction("📊", "History") {} }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Devices list
            Text(
                text = "Linked Devices",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.devices.isEmpty() -> {
                    EmptyState(onAddDevice = onAddDevice)
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.devices, key = { it.id }) { device ->
                            DeviceCard(
                                device = device,
                                onClick = { onDeviceClick(device.id) },
                                onConsentClick = { onConsentClick(device.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout?") },
            text = { Text("Aap logout karna chahte hain?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout { onLogout() }
                    }
                ) {
                    Text("Logout", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SummaryCard(title: String, value: String, icon: String, color: Color) {
    Card(
        modifier = Modifier.width(120.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(icon, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun QuickAction(icon: String, label: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(90.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun DeviceCard(
    device: Device,
    onClick: () -> Unit,
    onConsentClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Status dot
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(if (device.isOnline) OnlineGreen else OfflineRed)
                )
                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = device.deviceName ?: "Unknown Device",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${device.deviceType ?: "device"} • ${device.osVersion ?: ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    if (device.isOnline) Icons.Default.CheckCircle else Icons.Default.Close,
                    contentDescription = null,
                    tint = if (device.isOnline) OnlineGreen else OfflineRed
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Status",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        if (device.isOnline) "Online" else "Offline",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = if (device.isOnline) OnlineGreen else OfflineRed
                    )
                }

                Column {
                    Text(
                        "Permissions",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "${device.activeConsents} active",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                Column {
                    Text(
                        "Last Seen",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        DateTimeUtil.timeAgo(DateTimeUtil.parseIso(device.lastSeenAt)),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onConsentClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Consent")
                }
                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Open")
                }
            }
        }
    }
}

@Composable
fun EmptyState(onAddDevice: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("📱", style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Koi device linked nahi hai",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Add Device dabakar apna pehla device link karein",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onAddDevice) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Device")
        }
    }
}
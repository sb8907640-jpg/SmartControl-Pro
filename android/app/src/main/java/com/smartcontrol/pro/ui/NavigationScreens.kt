package com.smartcontrol.pro.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smartcontrol.pro.ui.profile.AppRole
import com.smartcontrol.pro.ui.profile.canAccessAdminPanel

@Composable fun DeviceDetailScreen(onBack: () -> Unit) { Column(Modifier.padding(16.dp)) { Text("Device detail", style = MaterialTheme.typography.headlineSmall); Button(onClick = onBack) { Text("Back") } } }
@Composable fun ConsentScreenStandalone() { Column(Modifier.padding(16.dp)) { Text("Consent Center", style = MaterialTheme.typography.headlineSmall); Button(onClick = {}) { Text("REVOKE ALL") } } }
@Composable fun AlertsScreenStandalone() { Column(Modifier.padding(16.dp)) { Text("Alerts", style = MaterialTheme.typography.headlineSmall) } }
@Composable fun ProfileScreenStandalone(role: AppRole, onAdminPanel: () -> Unit) { Column(Modifier.padding(16.dp)) { Text("Profile & Privacy", style = MaterialTheme.typography.headlineSmall); if (canAccessAdminPanel(role)) Button(onClick = onAdminPanel) { Text("Admin Panel") } } }
@Composable fun AdminPanelScreen(onBack: () -> Unit) { Column(Modifier.padding(16.dp)) { Text("Admin Panel", style = MaterialTheme.typography.headlineSmall); Text("Owner Settings"); Text("Team, roles, permissions, audit, billing and system settings"); Button(onClick = onBack) { Text("Back") } } }

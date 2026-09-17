package com.smartcontrol.pro.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smartcontrol.pro.ui.profile.AppRole
import com.smartcontrol.pro.ui.profile.canAccessAdminPanel

private val TrustBlue = Color(0xFF0066CC)
private val SafetyGreen = Color(0xFF00A86B)
private val DangerRed = Color(0xFFD93B3B)

data class LinkedDevice(val id: String, val name: String, val battery: Int, val online: Boolean, val consentCount: Int)

data class ConsentItem(val name: String, val description: String, val granted: Boolean)

@Composable
fun SmartControlApp(currentRole: AppRole, onAdminPanel: () -> Unit) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("Dashboard", "Devices", "Consent", "Alerts", "Profile")
    Scaffold(
        topBar = { SmartTopBar(onStopAll = {}) },
        bottomBar = { NavigationBar { tabs.forEachIndexed { index, title -> NavigationBarItem(selected = tab == index, onClick = { tab = index }, icon = { Icon(Icons.Default.Home, null) }, label = { Text(title) }) } } },
    ) { padding ->
        when (tab) {
            0 -> DashboardScreen(Modifier.padding(padding))
            1 -> DevicesScreen(Modifier.padding(padding))
            2 -> ConsentScreen(Modifier.padding(padding))
            3 -> AlertsScreen(Modifier.padding(padding))
            else -> ProfileScreen(Modifier.padding(padding), currentRole, onAdminPanel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable private fun SmartTopBar(onStopAll: () -> Unit) {
    TopAppBar(title = { Text("SmartControl Pro", fontWeight = FontWeight.Bold) }, actions = { IconButton(onClick = onStopAll) { Icon(Icons.Default.Stop, "STOP ALL", tint = DangerRed) } })
}

@Composable private fun DashboardScreen(modifier: Modifier = Modifier) {
    val devices = listOf(LinkedDevice("1", "Beti ka phone", 78, true, 5))
    LazyColumn(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Button(onClick = {}, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(TrustBlue)) { Text("+ ADD NEW DEVICE") } }
        item { Text("Quick Actions", style = MaterialTheme.typography.titleMedium) }
        item { ActionGrid() }
        item { Text("Linked Devices", style = MaterialTheme.typography.titleMedium) }
        items(devices) { DeviceCard(it) }
        item { Text("Alerts", style = MaterialTheme.typography.titleMedium) }
        item { AlertCard("No active alerts", SafetyGreen) }
    }
}

@Composable private fun DevicesScreen(modifier: Modifier = Modifier) { DashboardScreen(modifier) }

@Composable private fun ConsentScreen(modifier: Modifier = Modifier) {
    var items by remember { mutableStateOf(listOf(ConsentItem("Live location", "Share location with the owner", true), ConsentItem("Touch control", "Allow only after a visible session banner", false), ConsentItem("Keyboard input", "Allow only while the receiver can stop it", false))) }
    LazyColumn(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { Text("Consent Center", style = MaterialTheme.typography.headlineSmall) }
        item { Button(onClick = { items = items.map { it.copy(granted = false) } }, colors = ButtonDefaults.buttonColors(DangerRed), modifier = Modifier.fillMaxWidth()) { Text("REVOKE ALL") } }
        items(items) { item -> Card(Modifier.fillMaxWidth()) { Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Column(Modifier.weight(1f)) { Text(item.name, fontWeight = FontWeight.Bold); Text(item.description) }; Switch(checked = item.granted, onCheckedChange = { value -> items = items.map { if (it.name == item.name) it.copy(granted = value) else it } }) } } }
    }
}

@Composable private fun AlertsScreen(modifier: Modifier = Modifier) { Column(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { Text("Alerts", style = MaterialTheme.typography.headlineSmall); AlertCard("SOS, geofence and approval alerts appear here", TrustBlue) } }

@Composable private fun ProfileScreen(modifier: Modifier, role: AppRole, onAdminPanel: () -> Unit) { Column(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text("Profile & Privacy", style = MaterialTheme.typography.headlineSmall); Text("Role: $role"); OutlinedButton(onClick = {}) { Text("Audit Logs") }; OutlinedButton(onClick = {}) { Text("Data & Privacy") }; if (canAccessAdminPanel(role)) OutlinedButton(onClick = onAdminPanel) { Icon(Icons.Default.Lock, null); Spacer(Modifier.width(8.dp)); Text("Admin Panel") } } }

@Composable private fun ActionGrid() { Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) { listOf("Location", "Lock", "Message").forEach { OutlinedButton(onClick = {}, modifier = Modifier.weight(1f)) { Text(it) } } } }
@Composable private fun DeviceCard(device: LinkedDevice) { Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) { Text("● ${device.name}", color = if (device.online) SafetyGreen else Color.Gray, fontWeight = FontWeight.Bold); Text("Battery ${device.battery}% · ${device.consentCount} active consents"); Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("OPEN") } } } }
@Composable private fun AlertCard(text: String, color: Color) { Card(Modifier.fillMaxWidth()) { Text(text, Modifier.padding(16.dp), color = color) } }

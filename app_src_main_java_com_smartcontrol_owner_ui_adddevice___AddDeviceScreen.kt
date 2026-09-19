package com.smartcontrol.owner.ui.adddevice

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartcontrol.owner.data.model.InviteLink
import com.smartcontrol.owner.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeviceScreen(
    onBack: () -> Unit,
    onGenerateInvite: (String, List<String>, Int) -> Unit,
    viewModel: AddDeviceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Device") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            viewModel.generateInvite { invite ->
                                onGenerateInvite(
                                    invite.code,
                                    invite.permissions,
                                    uiState.selectedExpiryHours
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !uiState.isLoading && uiState.selectedPermissions.isNotEmpty()
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Generate Link", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Device Type
            item {
                Text(
                    "Device Type",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Auto-detect recommended hai. Manual bhi select kar sakte hain.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        DeviceTypeOption(
                            icon = "🔄",
                            label = "Auto-detect (Recommended)",
                            selected = uiState.selectedDeviceType == null,
                            onClick = { viewModel.selectDeviceType(null) }
                        )
                        Constants.DEVICE_TYPES.forEach { type ->
                            DeviceTypeOption(
                                icon = type.icon,
                                label = type.label,
                                selected = uiState.selectedDeviceType == type.id,
                                onClick = { viewModel.selectDeviceType(type.id) }
                            )
                        }
                    }
                }
            }

            // Permissions
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Permissions (${uiState.selectedPermissions.size}/${Constants.ALL_PERMISSIONS.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Receiver ye permissions allow/deny kar sakta hai",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { viewModel.selectAllPermissions() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Select All", style = MaterialTheme.typography.labelMedium)
                    }
                    OutlinedButton(
                        onClick = { viewModel.deselectAllPermissions() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear All", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            items(Constants.ALL_PERMISSIONS, key = { it.id }) { permission ->
                PermissionCheckCard(
                    icon = permission.icon,
                    title = permission.title,
                    description = permission.description,
                    checked = uiState.selectedPermissions.contains(permission.id),
                    onToggle = { viewModel.togglePermission(permission.id) }
                )
            }

            // Link Expiry
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Link Expiry",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "⚠ Link Expiry sirf INVITE LINK par lagu. Device link hone ke baad TAB TAK LINKED jab tak UNLINK na kare.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Constants.EXPIRY_OPTIONS.forEach { option ->
                            ExpiryOptionRow(
                                label = option.label,
                                selected = uiState.selectedExpiryHours == option.hours,
                                onClick = { viewModel.selectExpiry(option.hours) }
                            )
                        }
                    }
                }
            }

            // Error
            uiState.error?.let { error ->
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = error,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceTypeOption(
    icon: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        RadioButton(selected = selected, onClick = onClick)
    }
}

@Composable
fun PermissionCheckCard(
    icon: String,
    title: String,
    description: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Card(
        onClick = onToggle,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (checked)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Checkbox(checked = checked, onCheckedChange = { onToggle() })
        }
    }
}

@Composable
fun ExpiryOptionRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        RadioButton(selected = selected, onClick = onClick)
    }
}
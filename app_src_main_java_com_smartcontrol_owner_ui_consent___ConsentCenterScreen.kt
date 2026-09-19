package com.smartcontrol.owner.ui.consent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartcontrol.owner.data.model.Consent
import com.smartcontrol.owner.ui.theme.OfflineRed
import com.smartcontrol.owner.ui.theme.OnlineGreen
import com.smartcontrol.owner.util.Constants
import com.smartcontrol.owner.util.DateTimeUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsentCenterScreen(
    deviceId: String,
    onBack: () -> Unit,
    viewModel: ConsentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showRevokeAllDialog by remember { mutableStateOf(false) }

    LaunchedEffect(deviceId) {
        viewModel.loadConsents(deviceId)
    }

    val filtered = remember(uiState.consents, uiState.filter) {
        viewModel.getFilteredConsents()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Consent Center") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { showRevokeAllDialog = true },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Revoke All")
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Filter tabs
            TabRow(selectedTabIndex = when (uiState.filter) {
                "granted" -> 1
                "revoked" -> 2
                else -> 0
            }) {
                Tab(
                    selected = uiState.filter == "all",
                    onClick = { viewModel.setFilter("all") },
                    text = { Text("All") }
                )
                Tab(
                    selected = uiState.filter == "granted",
                    onClick = { viewModel.setFilter("granted") },
                    text = { Text("Granted") }
                )
                Tab(
                    selected = uiState.filter == "revoked",
                    onClick = { viewModel.setFilter("revoked") },
                    text = { Text("Revoked") }
                )
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                return@Column
            }

            // Consent cards — show all 19 permissions with current state
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(Constants.ALL_PERMISSIONS, key = { it.id }) { permission ->
                    val consent = uiState.consents
                        .filter { it.featureName == permission.id }
                        .maxByOrNull { it.grantedAt ?: "" }

                    val isGranted = consent?.granted == true && consent.revokedAt == null

                    ConsentPermissionCard(
                        icon = permission.icon,
                        title = permission.title,
                        description = permission.description,
                        isGranted = isGranted,
                        grantedAt = consent?.grantedAt,
                        onGrant = { viewModel.grantConsent(permission.id) },
                        onRevoke = { viewModel.revokeConsent(permission.id) }
                    )
                }
            }
        }
    }

    if (showRevokeAllDialog) {
        AlertDialog(
            onDismissRequest = { showRevokeAllDialog = false },
            title = { Text("Revoke All Permissions?") },
            text = { Text("Saari permissions revoke ho jayengi. Receiver ko notify jayega.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.revokeAll()
                        showRevokeAllDialog = false
                    }
                ) {
                    Text("Revoke All", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRevokeAllDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ConsentPermissionCard(
    icon: String,
    title: String,
    description: String,
    isGranted: Boolean,
    grantedAt: String?,
    onGrant: () -> Unit,
    onRevoke: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isGranted)
                OnlineGreen.copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                Icon(
                    if (isGranted) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = null,
                    tint = if (isGranted) OnlineGreen else OfflineRed
                )
            }

            if (isGranted && grantedAt != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Granted: ${DateTimeUtil.timeAgo(DateTimeUtil.parseIso(grantedAt))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isGranted) {
                    OutlinedButton(
                        onClick = onRevoke,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Revoke")
                    }
                } else {
                    Button(
                        onClick = onGrant,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Grant")
                    }
                }
            }
        }
    }
}
package com.smartcontrol.owner.ui.invite

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteScreen(
    onBack: () -> Unit,
    viewModel: InviteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showQrDialog by remember { mutableStateOf(false) }

    // Use last generated or first invite
    val invite = uiState.lastGenerated ?: uiState.invites.firstOrNull()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Invite Generated") },
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
        }
    ) { padding ->
        if (invite == null) {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Success banner
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("✅", style = MaterialTheme.typography.displayMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Link Ready!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Ab ise share karein",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Link details
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DetailRow("Link", invite.url, isMonospace = true)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    DetailRow("Short Code", invite.shortCode, isMonospace = true)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    DetailRow("Expires", invite.expiresAt)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    DetailRow("Device Type", invite.deviceType ?: "Auto-detected")
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    DetailRow("Permissions", "${invite.permissions.size} selected")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Share buttons
            Text(
                "Share via",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ShareButton(
                    icon = Icons.Default.ContentCopy,
                    label = "Copy",
                    modifier = Modifier.weight(1f)
                ) {
                    copyToClipboard(context, invite.url)
                }
                ShareButton(
                    icon = Icons.Default.Share,
                    label = "Share",
                    modifier = Modifier.weight(1f)
                ) {
                    shareText(context, invite.url)
                }
                ShareButton(
                    icon = Icons.Default.QrCode,
                    label = "QR Code",
                    modifier = Modifier.weight(1f)
                ) {
                    showQrDialog = true
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ShareButton(
                    icon = Icons.Default.Message,
                    label = "WhatsApp",
                    modifier = Modifier.weight(1f)
                ) {
                    shareViaWhatsApp(context, invite.url)
                }
                ShareButton(
                    icon = I
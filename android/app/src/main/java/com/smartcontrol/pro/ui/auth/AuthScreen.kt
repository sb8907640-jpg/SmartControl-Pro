package com.smartcontrol.pro.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smartcontrol.pro.auth.AuthState

@Composable
fun AuthScreen(state: AuthState, onGoogle: () -> Unit, onPhone: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("SmartControl Pro", style = MaterialTheme.typography.headlineMedium)
        Text("Sign in to continue. Device features require explicit receiver consent.")
        Button(onClick = onGoogle, modifier = Modifier.fillMaxWidth()) { Text("Continue with Google") }
        OutlinedButton(onClick = onPhone, modifier = Modifier.fillMaxWidth()) { Text("Continue with Phone") }
        if (state is AuthState.Error) Text(state.message, color = MaterialTheme.colorScheme.error)
    }
}

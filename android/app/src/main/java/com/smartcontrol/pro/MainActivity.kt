package com.smartcontrol.pro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.auth.FirebaseAuth
import com.smartcontrol.pro.auth.AuthState
import com.smartcontrol.pro.auth.AuthViewModel
import com.smartcontrol.pro.ui.SmartControlRoot
import com.smartcontrol.pro.ui.auth.AuthScreen
import com.smartcontrol.pro.ui.profile.AppRole

class MainActivity : ComponentActivity() {
    private val authViewModel = AuthViewModel()
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { val state by authViewModel.state.collectAsState(); when (state) { is AuthState.SignedIn -> SmartControlRoot(AppRole.valueOf((state as AuthState.SignedIn).role), authViewModel::signOut); else -> AuthScreen(state, onGoogle = { /* launch Google credential flow */ }, onPhone = { /* launch Firebase phone verification flow */ }) } } }
}

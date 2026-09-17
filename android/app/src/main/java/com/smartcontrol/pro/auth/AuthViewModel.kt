package com.smartcontrol.pro.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface AuthState { data object SignedOut : AuthState; data object Loading : AuthState; data class SignedIn(val uid: String, val role: String) : AuthState; data class Error(val message: String) : AuthState }

class AuthViewModel(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) : ViewModel() {
    private val _state = MutableStateFlow<AuthState>(AuthState.SignedOut)
    val state = _state.asStateFlow()
    init { auth.addAuthStateListener { user -> if (user == null) _state.value = AuthState.SignedOut else refreshRole() } }
    fun signInWithCredential(credential: com.google.firebase.auth.AuthCredential) { _state.value = AuthState.Loading; auth.signInWithCredential(credential).addOnFailureListener { _state.value = AuthState.Error(it.message ?: "Sign-in failed") }.addOnSuccessListener { refreshRole() } }
    fun signInWithGoogleIdToken(idToken: String) = signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
    fun refreshRole() { auth.currentUser?.getIdToken(true)?.addOnSuccessListener { result -> _state.value = AuthState.SignedIn(auth.currentUser!!.uid, result.claims["role"] as? String ?: "USER") }?.addOnFailureListener { _state.value = AuthState.Error("Could not load role") } }
    fun signOut() { auth.signOut(); _state.value = AuthState.SignedOut }
}

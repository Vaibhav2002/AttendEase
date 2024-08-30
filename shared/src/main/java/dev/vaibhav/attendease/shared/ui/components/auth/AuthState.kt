package dev.vaibhav.attendease.shared.ui.components.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

sealed interface AuthState {

    data object Idle: AuthState

    data object Loading : AuthState

    data class Success(val account: GoogleSignInAccount) : AuthState

    data class Error(val error: Throwable, ) : AuthState

}
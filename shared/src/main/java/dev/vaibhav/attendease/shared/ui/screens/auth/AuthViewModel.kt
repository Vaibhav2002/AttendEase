package dev.vaibhav.attendease.shared.ui.screens.auth

import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vaibhav.attendease.shared.data.repo.AuthRepository
import dev.vaibhav.attendease.shared.ui.components.auth.AuthState
import dev.vaibhav.attendease.shared.ui.screens.BaseViewModel
import dev.vaibhav.attendease.shared.ui.screens.ScreenState
import dev.vaibhav.attendease.shared.utils.auth.AuthException
import dev.vaibhav.attendease.shared.utils.auth.GoogleSignInClient
import dev.vaibhav.attendease.shared.utils.onIO
import dev.vaibhav.attendease.shared.utils.safeCatch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val signInClient: GoogleSignInClient,
    private val authRepo: AuthRepository
) : BaseViewModel() {

    private val _authSuccess = MutableSharedFlow<Unit>()
    val authSuccess = _authSuccess.asSharedFlow()

    fun handleAuthStateChange(state: AuthState) {
        when(state){
            is AuthState.Success -> login(state.account)
            is AuthState.Error -> {
                if(state.error is AuthException) showSnackBar(state.error.message)
                else showSnackBar()
            }
            else -> Unit
        }
    }

    private fun login(
        account: GoogleSignInAccount
    ) = flow { emit(authRepo.signInUsingGoogle(account)) }
        .onStart { setScreenState(ScreenState.Loading) }
        .onEach { showSnackBar("Login Successful") }
        .onEach { setScreenState(ScreenState.Normal) }
        .onEach { _authSuccess.emit(Unit) }
        .safeCatch {
            if(it is AuthException) showSnackBar(it.message)
            else showSnackBar()
            setScreenState(ScreenState.Normal)
        }
        .onIO()
        .launchIn(viewModelScope)

}
package dev.vaibhav.attendease.shared.ui.screens.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vaibhav.attendease.shared.data.repo.AuthRepository
import dev.vaibhav.attendease.shared.ui.screens.BaseViewModel
import dev.vaibhav.attendease.shared.utils.onIO
import dev.vaibhav.attendease.shared.utils.safeCatch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : BaseViewModel() {

    val user = authRepo.user

    private val _logoutComplete = MutableSharedFlow<Unit>()
    val logoutComplete = _logoutComplete.asSharedFlow()

    fun logout() = flow { emit(authRepo.logout()) }
        .onEach { _logoutComplete.emit(Unit) }
        .onIO()
        .safeCatch { showSnackBar() }
        .launchIn(viewModelScope)

}
package dev.vaibhav.attendease.student

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vaibhav.attendease.shared.data.repo.AuthRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepo: AuthRepository
): ViewModel() {

    val isLoggedIn
        get() = authRepo.isLoggedIn
}
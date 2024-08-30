package dev.vaibhav.attendease.shared.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    state: ScreenState = ScreenState.Normal
) : ViewModel() {

    private val _screenState = MutableStateFlow(state)
    val screenState = _screenState.asStateFlow()

    private val _snackBarEvent = MutableSharedFlow<String>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    private val _retry = MutableSharedFlow<Unit>()
    val retry = _retry.asSharedFlow()

    fun showSnackBar(message: String = "Oops! Something went wrong") = viewModelScope.launch {
        _snackBarEvent.emit(message)
    }

    fun setScreenState(state: ScreenState) {
        _screenState.update { state }
    }

    fun setErrorState(error: Exception) {
        setScreenState(ScreenState.Error(error))
    }

    fun retry() = viewModelScope.launch {
        _retry.emit(Unit)
    }
}
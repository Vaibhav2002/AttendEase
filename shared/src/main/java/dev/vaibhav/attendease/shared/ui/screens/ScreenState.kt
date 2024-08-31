package dev.vaibhav.attendease.shared.ui.screens

sealed interface ScreenState {
    data object Normal: ScreenState
    data object Loading: ScreenState
    data class Error(val exception: Throwable): ScreenState
}
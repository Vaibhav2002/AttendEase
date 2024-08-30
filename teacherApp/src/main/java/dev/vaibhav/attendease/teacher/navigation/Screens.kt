package dev.vaibhav.attendease.teacher.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screens {

    @Serializable
    data object Auth : Screens
}
package dev.vaibhav.attendease.student.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screens {

    @Serializable
    data object Auth : Screens

    @Serializable
    data object Home : Screens

    @Serializable
    data object QR: Screens

    @Serializable
    data class Subject(val id: String, val name: String) : Screens

    @Serializable
    data object Profile : Screens
}
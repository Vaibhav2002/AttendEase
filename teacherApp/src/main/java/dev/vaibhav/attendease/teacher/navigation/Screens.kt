package dev.vaibhav.attendease.teacher.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screens {

    @Serializable
    data object Auth : Screens

    @Serializable
    data object Home : Screens

    @Serializable
    data class Classes(val subjectId: String) : Screens

    @Serializable
    data class Attendance(val classId: String) : Screens
}
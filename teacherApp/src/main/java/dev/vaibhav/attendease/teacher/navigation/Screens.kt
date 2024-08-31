package dev.vaibhav.attendease.teacher.navigation

import dev.vaibhav.attendease.shared.data.models.Subject
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
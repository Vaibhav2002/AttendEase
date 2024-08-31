package dev.vaibhav.attendease.shared.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Attendance(
    val studentId: String = "",
    val classId: String = "",
){
    val id: String = "$studentId-$classId"
}
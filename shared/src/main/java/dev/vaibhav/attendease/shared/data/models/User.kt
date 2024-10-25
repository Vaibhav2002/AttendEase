package dev.vaibhav.attendease.shared.data.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val profilePic: String = "",
    val role: Role = Role.STUDENT
)

val User.rollNumber
    get() = id
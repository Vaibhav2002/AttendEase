package dev.vaibhav.attendease.shared.data.models

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val profilePic: String = "",
    val role: Role = Role.STUDENT
)

val User.rollNumber
    get() = email.substringBefore('@').uppercase()
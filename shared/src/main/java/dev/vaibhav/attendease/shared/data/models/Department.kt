package dev.vaibhav.attendease.shared.data.models

import kotlinx.serialization.Serializable

@Serializable
enum class Department {
    IT, CSE, ECE, EE;
}

val Department.label: String
    get() = when (this) {
        Department.IT -> "Information Technology"
        Department.CSE -> "Computer Science"
        Department.ECE -> "Electronics and Communication"
        Department.EE -> "Electrical Engineering"
    }
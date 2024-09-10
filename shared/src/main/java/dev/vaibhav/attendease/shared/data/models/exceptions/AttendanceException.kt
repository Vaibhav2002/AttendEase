package dev.vaibhav.attendease.shared.data.models.exceptions

sealed class AttendanceException(message: String) : Exception(message) {
    data object AlreadyMarked : AttendanceException("Student already marked present")
}
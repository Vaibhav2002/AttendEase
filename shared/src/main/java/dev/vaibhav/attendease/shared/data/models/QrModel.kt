package dev.vaibhav.attendease.shared.data.models

import kotlinx.serialization.Serializable

@Serializable
data class QrModel(
    val studentId: String,
    val email: String,
    val createdAt: Long
)
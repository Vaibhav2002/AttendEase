package dev.vaibhav.attendease.shared.data.models

import dev.vaibhav.attendease.shared.utils.DateHelpers
import kotlinx.serialization.Serializable

@Serializable
data class Class(
    val id: String = "",
    val subjectId: String = "",
    val createdOn: Long = 0L,
    val attendees: List<String> = emptyList()
)


val Class.createdAt
    get() = DateHelpers.toInstant(createdOn)


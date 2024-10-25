package dev.vaibhav.attendease.shared.data.models

import androidx.annotation.Keep
import dev.vaibhav.attendease.shared.utils.DateHelpers
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Subject @Keep constructor(
    val id: String = "",
    val createdBy: String = "",
    val title: String = "",
    val department: Department = Department.IT,
    val section: Section = Section.A,
    val created: Long = 0L,
    val studentsEnrolled: List<String> = emptyList(),
    val creatorName: String = ""
)

val Subject.createdAt: Instant
    get() = DateHelpers.toInstant(created)
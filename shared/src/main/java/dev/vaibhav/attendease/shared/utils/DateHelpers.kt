package dev.vaibhav.attendease.shared.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration

object DateHelpers {

    val now: Instant
        get() = Clock.System.now()

    val nowInMillis: Long
        get() = now.toEpochMilliseconds()

    fun toEpochMillis(instant: String): Long {
        return instant.toInstant().toEpochMilliseconds()
    }

    fun toInstant(localDateTime: LocalDateTime): String {
        return localDateTime.toInstant(TimeZone.currentSystemDefault()).toString()
    }

    fun toInstant(millis: Long): Instant {
        return Instant.fromEpochMilliseconds(millis)
    }

    fun toLocalDateTime(instant: Instant): LocalDateTime {
        return instant.toLocalDateTime(timeZone = TimeZone.currentSystemDefault())
    }

    fun getEpochDays(time: Instant): Int {
        return toLocalDateTime(time).date.toEpochDays()
    }

    fun toLocalDateTimeFromMillis(millis: Long): LocalDateTime {
        return Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun daysBetween(start: Instant, end: Instant): Int {
        val start = toLocalDateTime(start)
        val end = toLocalDateTime(end)
        return end.dayOfYear - start.dayOfYear
    }

    fun monthDate(time: Instant?): String {

        if (time == null) return ""

        val dateTime = time.toLocalDateTime(TimeZone.currentSystemDefault())
        val month = dateTime.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
        val year = dateTime.year

        return "$month $year"
    }

    fun fullDate(time: Instant, showYear: Boolean = true): String {
        val dateTime = time.toLocalDateTime(TimeZone.currentSystemDefault())
        val month = dateTime.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
        val day = if (dateTime.dayOfMonth < 10) "0${dateTime.dayOfMonth}" else dateTime.dayOfMonth
        val year = dateTime.year

        return if (showYear) "$day $month $year"
        else "$day $month"
    }

    fun formatDateTime(dateTime: LocalDateTime): String {
        val month = dateTime.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
        val day = if (dateTime.dayOfMonth < 10) "0${dateTime.dayOfMonth}" else dateTime.dayOfMonth
        val year = dateTime.year.mod(100)
        val hour = (if (dateTime.hour > 12) dateTime.hour - 12 else dateTime.hour)
        val minute = if (dateTime.minute < 10) "0${dateTime.minute}" else dateTime.minute

        val amPm = if (dateTime.hour < 12) "am" else "pm"

        return buildString {
            append(hour)
            append(":")
            append(minute)
            append(" ")
            append(amPm)
            append(" • ")
            append(day)
            append(" ")
            append(month)
            append(" ")
            append(year)
        }
    }

    fun formatTime(instant: Instant) = formatTime(toLocalDateTime(instant))

    fun formatDate(instant: Instant) = formatDateTime(toLocalDateTime(instant))

    fun formatTime(dateTime: LocalDateTime): String {
        val hour = (if (dateTime.hour > 12) dateTime.hour - 12 else dateTime.hour)
        val minute = if (dateTime.minute < 10) "0${dateTime.minute}" else dateTime.minute
        val amPm = if (dateTime.hour < 12) "am" else "pm"

        return buildString {
            append(hour)
            append(":")
            append(minute)
            append(" ")
            append(amPm)
        }
    }

    fun relativeTime(time: Instant, ago: Boolean = true): String {
        val curTime = nowInMillis
        val timeMillis = time.toEpochMilliseconds()
        val differenceMillis = curTime - timeMillis
        val seconds = differenceMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = days / 30
        val years = days / 365

        val agoText = if (ago) " ago" else ""

        return when {
            years > 0 -> "${years}y${agoText}"
            months > 0 -> "${months}mo${agoText}"
            weeks > 0 -> "${weeks}w${agoText}"
            days > 0 -> "${days}d${agoText}"
            hours > 0 -> "${hours}h${agoText}"
            minutes > 0 -> "${minutes}m${agoText}"
            else -> "Just now"
        }

    }

    fun relativeDay(time: Instant): String {
        val curDay = toLocalDateTime(now)
        val day = toLocalDateTime(time)
        val days = curDay.dayOfYear - day.dayOfYear
        return when (days) {
            0 -> "Today"
            1 -> "Yesterday"
            else -> fullDate(time, curDay.year != day.year)
        }
    }
}

fun Duration.formatCountdown(): String {

    val appendZero = { value: Int -> if (value < 10) "0$value" else value.toString() }

    val days = inWholeDays.toInt().let(appendZero)
    val hours = (inWholeHours % 24).toInt().let(appendZero)
    val minutes = (inWholeMinutes % 60).toInt().let(appendZero)
    val seconds = (inWholeSeconds % 60).toInt().let(appendZero)

    return buildString {
        append(days)
        append(" : ")
        append(hours)
        append(" : ")
        append(minutes)
        append(" : ")
        append(seconds)
    }
}

val Duration.formatMinutes:String
    get() {
        val minutes = this.inWholeMinutes
        val totalSeconds = this.inWholeSeconds
        val seconds = totalSeconds % 60
        return "$minutes:${if (seconds < 10) "0$seconds" else seconds}"
    }
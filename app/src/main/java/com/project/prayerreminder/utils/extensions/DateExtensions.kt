package com.project.prayerreminder.utils.extensions

import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun YearMonth.toMonthYear(locale: Locale): String {
    return format(
        DateTimeFormatter.ofPattern("MMMM yyyy", locale),
    )
}

fun DayOfWeek.toNarrowName(locale: Locale): String {
    return getDisplayName(
        TextStyle.NARROW,
        locale,
    )
}

data class DateParts(
    val day: Int,
    val month: Int,
    val year: Int,
)

// Converts a "DD-MM-YYYY" date into separate numeric components.
fun String.toDatePartsOrNull(): DateParts? {
    val parts = split("-")

    if (parts.size != 3) return null

    val day = parts[0].toIntOrNull() ?: return null
    val month = parts[1].toIntOrNull() ?: return null
    val year = parts[2].toIntOrNull() ?: return null

    if (day !in 1..31 || month !in 1..12 || year <= 0) {
        return null
    }

    return DateParts(
        day = day,
        month = month,
        year = year,
    )
}
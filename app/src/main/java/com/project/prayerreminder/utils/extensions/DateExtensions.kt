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
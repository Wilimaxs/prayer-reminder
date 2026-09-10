package com.project.prayerreminder.utils.extensions

import com.project.prayerreminder.utils.enumeration.AppLanguage
import java.time.DayOfWeek
import java.time.LocalDate
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

// Converts a "DD-MM-YYYY" date from ROOM into a localized Gregorian date.
fun String.toLocalizedGregorianDate(
    language: AppLanguage,
): String {
    val inputFormatter = DateTimeFormatter.ofPattern("dd-MM-uuuu")
    val locale = Locale.forLanguageTag(language.languageTag)

    val outputPattern = when (language) {
        AppLanguage.English -> "MMMM dd, uuuu"
        AppLanguage.Indonesian -> "dd MMMM uuuu"
    }

    return runCatching {
        LocalDate.parse(this, inputFormatter).format(
            DateTimeFormatter.ofPattern(outputPattern, locale),
        )
    }.getOrDefault(this)
}

// Converts a "DD-MM-YYYY" date from ROOM into hijri month and localized.
fun formatHijriDate(
    day: Int,
    month: Int,
    year: Int,
    language: AppLanguage,
): String {
    val englishMonths = listOf(
        "Muharram",
        "Safar",
        "Rabi al-Awwal",
        "Rabi al-Thani",
        "Jumada al-Awwal",
        "Jumada al-Thani",
        "Rajab",
        "Sha'ban",
        "Ramadan",
        "Shawwal",
        "Dhu al-Qi'dah",
        "Dhu al-Hijjah",
    )

    val indonesianMonths = listOf(
        "Muharam",
        "Safar",
        "Rabiulawal",
        "Rabiulakhir",
        "Jumadilawal",
        "Jumadilakhir",
        "Rajab",
        "Syakban",
        "Ramadan",
        "Syawal",
        "Zulkaidah",
        "Zulhijah",
    )

    val months = when (language) {
        AppLanguage.English -> englishMonths
        AppLanguage.Indonesian -> indonesianMonths
    }

    val monthName = months.getOrNull(month - 1) ?: month.toString()

    return "$day $monthName $year H"
}
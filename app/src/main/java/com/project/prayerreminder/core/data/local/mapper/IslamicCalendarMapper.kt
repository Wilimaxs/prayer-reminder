package com.project.prayerreminder.core.data.local.mapper

import com.project.prayerreminder.core.data.local.entity.IslamicCalendarEntity
import com.project.prayerreminder.core.data.remote.model.CalendarDataDto
import com.project.prayerreminder.utils.extensions.toDatePartsOrNull

fun CalendarDataDto.toIslamicCalendarEntity(): IslamicCalendarEntity? {
    val dateInfo = date ?: return null
    val hijriDateInfo = dateInfo.hijri ?: return null

    val gregorianDateValue = dateInfo.gregorian
        ?.date
        ?.takeIf { it.isNotBlank() }
        ?: return null

    // Splits the Gregorian date for Room filtering and ordering.
    val gregorianDate = gregorianDateValue.toDatePartsOrNull()
        ?: return null

    // Splits the Hijri date for localized calendar presentation.
    val hijriDate = hijriDateInfo.date
        ?.toDatePartsOrNull()
        ?: return null

    return IslamicCalendarEntity(
        date = gregorianDateValue,
        gregorianDay = gregorianDate.day,
        gregorianMonth = gregorianDate.month,
        gregorianYear = gregorianDate.year,
        hijriDay = hijriDate.day,
        hijriMonth = hijriDate.month,
        hijriYear = hijriDate.year,
        islamicEvents = hijriDateInfo.holidays
            .orEmpty()
            .filter { it.isNotBlank() }
            .distinct()
    )
}
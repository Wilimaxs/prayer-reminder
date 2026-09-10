package com.project.prayerreminder.feature.calendar.mapper

import com.project.prayerreminder.core.data.local.entity.IslamicCalendarEntity
import com.project.prayerreminder.core.data.local.entity.PersonalScheduleEntity
import com.project.prayerreminder.feature.calendar.CalendarIslamicEventUiState
import com.project.prayerreminder.feature.calendar.CalendarPersonalScheduleUiState
import com.project.prayerreminder.utils.enumeration.AppLanguage
import com.project.prayerreminder.utils.extensions.formatHijriDate

// Maps all Islamic events returned by the API for one selected date.
fun IslamicCalendarEntity.toIslamicEventUiStates(
    language: AppLanguage,
): List<CalendarIslamicEventUiState> {
    val localizedHijriDate = formatHijriDate(
        day = hijriDay,
        month = hijriMonth,
        year = hijriYear,
        language = language,
    )

    return islamicEvents.map { event ->
        CalendarIslamicEventUiState(
            title = event,
            hijriDate = localizedHijriDate,
        )
    }
}

// Maps a personal schedule into values displayed by Calendar.
fun PersonalScheduleEntity.toCalendarUiState(): CalendarPersonalScheduleUiState {
    return CalendarPersonalScheduleUiState(
        id = id,
        title = title,
        time = scheduleTime,
        isReminderEnabled = isReminderEnabled,
    )
}

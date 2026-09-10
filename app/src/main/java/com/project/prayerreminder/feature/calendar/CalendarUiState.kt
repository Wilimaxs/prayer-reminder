package com.project.prayerreminder.feature.calendar

import java.time.LocalDate

data class CalendarUiState(
    val isLoading: Boolean = true,
    val selectedDate: LocalDate = LocalDate.now(),
    val islamicEvents: List<CalendarIslamicEventUiState> = emptyList(),
    val personalSchedules: List<CalendarPersonalScheduleUiState> = emptyList(),
    val errorMessage: String? = null,
)

data class CalendarIslamicEventUiState(
    val title: String,
    val hijriDate: String,
)

data class CalendarPersonalScheduleUiState(
    val id: Long,
    val title: String,
    val time: String,
    val isReminderEnabled: Boolean,
)

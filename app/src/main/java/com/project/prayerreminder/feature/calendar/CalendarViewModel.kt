package com.project.prayerreminder.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.prayerreminder.core.data.local.pref.DataStoreManager
import com.project.prayerreminder.core.data.local.pref.PreferenceKeys
import com.project.prayerreminder.core.data.repository.PersonalScheduleRepository
import com.project.prayerreminder.core.data.repository.PrayerRepository
import com.project.prayerreminder.feature.calendar.mapper.toCalendarUiState
import com.project.prayerreminder.feature.calendar.mapper.toIslamicEventUiStates
import com.project.prayerreminder.utils.enumeration.AppLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val prayerRepository: PrayerRepository,
    private val personalScheduleRepository: PersonalScheduleRepository,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val selectedDate = MutableStateFlow(LocalDate.now())

    // Replaces both Room observations whenever the selected date changes.
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CalendarUiState> = selectedDate
        .flatMapLatest { date ->
            val formattedDate = date.format(DATE_FORMATTER)

            combine(
                prayerRepository.observeIslamicCalendarByDate(formattedDate),
                personalScheduleRepository.observeSchedulesByDate(formattedDate),
                dataStoreManager.get(
                    key = PreferenceKeys.APP_LANGUAGE,
                    defaultValue = AppLanguage.English.languageTag,
                ),
            ) { islamicCalendar, personalSchedules, languageTag ->
                val language = AppLanguage.fromLanguageTag(languageTag)

                CalendarUiState(
                    isLoading = false,
                    selectedDate = date,
                    islamicEvents = islamicCalendar
                        ?.toIslamicEventUiStates(language)
                        .orEmpty(),
                    personalSchedules = personalSchedules.map { schedule ->
                        schedule.toCalendarUiState()
                    },
                )
            }.onStart {
                // Clears content from the previous date before Room emits new data.
                emit(
                    CalendarUiState(
                        isLoading = true,
                        selectedDate = date,
                    )
                )
            }
        }.catch { error ->
            Timber.e(error, "Failed to observe Calendar data")
            emit(
                CalendarUiState(
                    isLoading = false,
                    selectedDate = selectedDate.value,
                    errorMessage = error.message,
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CalendarUiState(),
        )

    fun selectDate(date: LocalDate) {
        selectedDate.value = date
    }

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    }
}

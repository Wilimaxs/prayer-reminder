package com.project.prayerreminder.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.prayerreminder.core.data.local.pref.DataStoreManager
import com.project.prayerreminder.core.data.local.pref.PreferenceKeys
import com.project.prayerreminder.core.data.repository.PrayerRepository
import com.project.prayerreminder.feature.home.mapper.toHomeContentUiState
import com.project.prayerreminder.utils.enumeration.AppLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    prayerRepository: PrayerRepository,
    dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val currentDateTime = flow {
        while (currentCoroutineContext().isActive) {
            emit(LocalDateTime.now())
            delay(TIME_UPDATE_INTERVAL)
        }
    }

    val uiState: StateFlow<HomeUiState> = combine(
        prayerRepository.observePrayerSchedules(),
        dataStoreManager.get(
            key = PreferenceKeys.APP_LANGUAGE,
            defaultValue = AppLanguage.English.languageTag,
        ),
        dataStoreManager.get(
            key = PreferenceKeys.CITY_NAME,
            defaultValue = DEFAULT_LOCATION_NAME,
        ),
        currentDateTime,
    ) { schedules, languageTag, locationName, dateTime ->
        val today = dateTime.toLocalDate()
        val todaySchedule = schedules.firstOrNull { schedule ->
            schedule.date == today.format(DATE_FORMATTER)
        }
        val nextDaySchedule = schedules.firstOrNull { schedule ->
            schedule.date == today.plusDays(1).format(DATE_FORMATTER)
        }

        HomeUiState(
            isLoading = false,
            content = todaySchedule?.toHomeContentUiState(
                nextDaySchedule = nextDaySchedule,
                currentDateTime = dateTime,
                locationName = locationName,
                language = AppLanguage.fromLanguageTag(languageTag),
            ),
        )
    }.catch { error ->
        Timber.e(error, "Failed to observe Home data")
        emit(
            HomeUiState(
                isLoading = false,
                errorMessage = error.message,
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeUiState(),
    )

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        private const val TIME_UPDATE_INTERVAL = 60_000L
        private const val DEFAULT_LOCATION_NAME = "Jakarta, Indonesia"
    }
}

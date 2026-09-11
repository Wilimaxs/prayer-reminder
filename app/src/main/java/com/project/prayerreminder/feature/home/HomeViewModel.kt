package com.project.prayerreminder.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.prayerreminder.core.data.local.pref.DataStoreManager
import com.project.prayerreminder.core.data.local.pref.PreferenceKeys
import com.project.prayerreminder.core.data.repository.PrayerRepository
import com.project.prayerreminder.core.firebase.RemoteConfigManager
import com.project.prayerreminder.core.location.LocationAddressResolver
import com.project.prayerreminder.feature.home.mapper.toHomeContentUiState
import com.project.prayerreminder.utils.enumeration.AppLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class HomeViewModel @Inject constructor(
    prayerRepository: PrayerRepository,
    private val dataStoreManager: DataStoreManager,
    private val locationAddressResolver: LocationAddressResolver,
    remoteConfigManager: RemoteConfigManager,
) : ViewModel() {

    private val remoteAnnouncement = flow {
        val config = remoteConfigManager.currentConfig
        val lastSeenId = dataStoreManager.get(
            key = PreferenceKeys.LAST_SEEN_ANNOUNCEMENT_ID,
            defaultValue = "",
        ).first()

        emit(
            if (config.announcementId.isNotBlank() &&
                config.announcementMessage.isNotBlank() &&
                config.announcementId != lastSeenId
            ) {
                config.announcementId to config.announcementMessage
            } else {
                null
            }
        )
    }

    // Shares one Room observation between Home content and location resolution.
    private val prayerSchedules = prayerRepository.observePrayerSchedules()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 1,
        )

    private val appLanguage = dataStoreManager.get(
        key = PreferenceKeys.APP_LANGUAGE,
        defaultValue = AppLanguage.English.languageTag,
    ).map(AppLanguage::fromLanguageTag)
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            replay = 1,
        )

    private val prayerRemindersEnabled = dataStoreManager.get(
        key = PreferenceKeys.PRAYER_REMINDERS_ENABLED,
        defaultValue = false,
    ).shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 1,
    )

    private val currentDateTime = flow {
        while (currentCoroutineContext().isActive) {
            emit(LocalDateTime.now())
            delay(TIME_UPDATE_INTERVAL)
        }
    }

    // Resolves the location again only when its coordinates or language change.
    @OptIn(ExperimentalCoroutinesApi::class)
    private val locationName = combine(
        prayerSchedules,
        appLanguage,
    ) { schedules, language ->
        schedules.firstOrNull()?.let { schedule ->
            LocationLookup(
                latitude = schedule.latitude,
                longitude = schedule.longitude,
                language = language,
            )
        }
    }.distinctUntilChanged()
        .transformLatest { location ->
            if (location == null) {
                emit("")
                return@transformLatest
            }

            val fallbackName = if (location.isDefaultLocation()) {
                DEFAULT_LOCATION_NAME
            } else {
                formatCoordinateLabel(
                    latitude = location.latitude,
                    longitude = location.longitude,
                )
            }

            // Displays a fallback immediately while Geocoder resolves the city.
            emit(fallbackName)

            if (!location.isDefaultLocation()) {
                locationAddressResolver.resolve(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    locale = Locale.forLanguageTag(location.language.languageTag),
                )?.toDisplayName()
                    ?.takeIf { it.isNotBlank() }
                    ?.let { emit(it) }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = "",
        )

    val uiState: StateFlow<HomeUiState> = combine(
        prayerSchedules,
        appLanguage,
        prayerRemindersEnabled,
        locationName,
        currentDateTime,
    ) { schedules, language, isReminderEnabled, locationName, dateTime ->
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
                language = language,
                isReminderEnabled = isReminderEnabled,
            ),
        )
    }.combine(remoteAnnouncement) { state, announcement ->
        state.copy(
            announcementId = announcement?.first,
            announcementMessage = announcement?.second,
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

    // Prevents the same Remote Config announcement from appearing again.
    fun markAnnouncementAsSeen(announcementId: String) {
        viewModelScope.launch {
            try {
                dataStoreManager.save(
                    key = PreferenceKeys.LAST_SEEN_ANNOUNCEMENT_ID,
                    value = announcementId,
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Timber.e(exception, "Failed to save the viewed announcement")
            }
        }
    }

    private fun LocationLookup.isDefaultLocation(): Boolean {
        return latitude == DEFAULT_LATITUDE && longitude == DEFAULT_LONGITUDE
    }

    private fun formatCoordinateLabel(
        latitude: Double,
        longitude: Double,
    ): String {
        return String.format(
            Locale.US,
            "%.4f, %.4f",
            latitude,
            longitude,
        )
    }

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        private const val TIME_UPDATE_INTERVAL = 60_000L
        private const val DEFAULT_LATITUDE = -6.2088
        private const val DEFAULT_LONGITUDE = 106.8456
        private const val DEFAULT_LOCATION_NAME = "Jakarta, Indonesia"
    }
}

private data class LocationLookup(
    val latitude: Double,
    val longitude: Double,
    val language: AppLanguage,
)

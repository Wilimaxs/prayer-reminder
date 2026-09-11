package com.project.prayerreminder.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.prayerreminder.BuildConfig
import com.project.prayerreminder.core.alarm.AlarmScheduler
import com.project.prayerreminder.core.data.local.entity.PrayerEntity
import com.project.prayerreminder.core.data.local.pref.DataStoreManager
import com.project.prayerreminder.core.data.local.pref.PreferenceKeys
import com.project.prayerreminder.core.data.repository.PrayerRepository
import com.project.prayerreminder.core.firebase.RemoteConfigManager
import com.project.prayerreminder.core.onError
import com.project.prayerreminder.core.onLoading
import com.project.prayerreminder.core.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val prayerRepository: PrayerRepository,
    private val dataStoreManager: DataStoreManager,
    private val alarmScheduler: AlarmScheduler,
    private val remoteConfigManager: RemoteConfigManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState = _uiState.asStateFlow()

    private var initializationJob: Job? = null
    private var progressJob: Job? = null
    private var lastLatitude: Double? = null
    private var lastLongitude: Double? = null
    private var cachedPrayerBeforeLocation: PrayerEntity? = null
    private var lastShouldCompareLocation = false

    init {
        checkRemoteConfig()
    }

    // Checks whether the application may continue before location and cache work starts.
    private fun checkRemoteConfig() {
        _uiState.update { currentState ->
            currentState.copy(
                isInitializing = true,
                progressMessage = SplashProgressMessage.CheckingApplication,
                errorMessage = null,
            )
        }

        viewModelScope.launch {
            val config = remoteConfigManager.fetchAndActivate()
            val currentVersion = BuildConfig.VERSION_CODE.toLong()
            val remoteDialog = when {
                config.maintenanceEnabled -> SplashRemoteDialog.Maintenance
                currentVersion < config.minimumSupportedVersion -> {
                    SplashRemoteDialog.ForceUpdate
                }

                currentVersion < config.latestVersion -> SplashRemoteDialog.SoftUpdate
                else -> null
            }

            if (remoteDialog == null) {
                checkCacheBeforeLocation()
            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        isInitializing = false,
                        remoteDialog = remoteDialog,
                    )
                }
            }
        }
    }

    // Continues startup when the optional update is postponed.
    fun continueAfterOptionalUpdate() {
        _uiState.update { currentState ->
            currentState.copy(remoteDialog = null)
        }
        checkCacheBeforeLocation()
    }

    // Checks Room before deciding whether location permission is required.
    private fun checkCacheBeforeLocation() {
        _uiState.update { currentState ->
            currentState.copy(
                isInitializing = true,
                progressMessage = SplashProgressMessage.CheckingPrayerSchedule,
                errorMessage = null,
            )
        }

        startProgress()

        viewModelScope.launch {
            try {
                val currentDate = LocalDate.now()
                val formattedDate = currentDate.format(DATE_FORMATTER)

                cachedPrayerBeforeLocation = prayerRepository.getPrayerScheduleByDate(
                    date = formattedDate,
                )

                val hasIslamicCalendar = prayerRepository.hasIslamicCalendarForYear(
                    year = currentDate.year,
                )

                val requiresLocation = cachedPrayerBeforeLocation == null ||
                        !hasIslamicCalendar

                _uiState.update { currentState ->
                    currentState.copy(
                        progressMessage = SplashProgressMessage.CheckingLocation,
                        locationCheckMode = if (requiresLocation) {
                            SplashLocationCheckMode.Required
                        } else {
                            SplashLocationCheckMode.Optional
                        },
                    )
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                Timber.e(error, "Failed to check cached startup data")
                finishWithError("Unable to check cached prayer data.")
            }
        }
    }

    // Uses cache silently when location is optional, otherwise displays the fallback dialog.
    fun onLocationUnavailable(
        settingsTarget: LocationSettingsTarget,
    ) {
        when (_uiState.value.locationCheckMode) {
            SplashLocationCheckMode.Optional -> {
                val cachedPrayer = cachedPrayerBeforeLocation

                if (cachedPrayer != null) {
                    initializeApp(
                        latitude = cachedPrayer.latitude,
                        longitude = cachedPrayer.longitude,
                        shouldCompareLocation = false,
                    )
                }
            }

            SplashLocationCheckMode.Required -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        showLocationDialog = true,
                        locationCheckMode = null,
                        locationSettingsTarget = settingsTarget,
                        progressMessage = SplashProgressMessage.CheckingLocation,
                    )
                }
            }

            null -> Unit
        }
    }

    // Hides the location dialog while Android Settings is open.
    fun onLocationSettingsOpened() {
        _uiState.update { currentState ->
            currentState.copy(
                showLocationDialog = false,
                locationCheckMode = null,
            )
        }
    }

    // Requests another required location check after returning from Settings.
    fun onLocationSettingsReturned() {
        _uiState.update { currentState ->
            currentState.copy(
                locationCheckMode = SplashLocationCheckMode.Required,
                progressMessage = SplashProgressMessage.CheckingLocation,
            )
        }
    }

    // Starts initialization using the coordinates returned by the device.
    fun onLocationAvailable(
        latitude: Double,
        longitude: Double,
    ) {
        initializeApp(
            latitude = latitude,
            longitude = longitude,
            shouldCompareLocation = true,
        )
    }

    // Continues initialization with Jakarta when the location is unavailable.
    fun continueWithDefaultLocation() {
        initializeApp(
            latitude = DEFAULT_LATITUDE,
            longitude = DEFAULT_LONGITUDE,
            shouldCompareLocation = false,
        )
    }

    // Calculates the distance between two coordinates using the Haversine formula.
    private fun calculateDistanceKm(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double,
    ): Double {
        val latitudeDistance = Math.toRadians(endLatitude - startLatitude)
        val longitudeDistance = Math.toRadians(endLongitude - startLongitude)

        val startLatitudeRadians = Math.toRadians(startLatitude)
        val endLatitudeRadians = Math.toRadians(endLatitude)

        val calculation = sin(latitudeDistance / 2) *
                sin(latitudeDistance / 2) +
                cos(startLatitudeRadians) *
                cos(endLatitudeRadians) *
                sin(longitudeDistance / 2) *
                sin(longitudeDistance / 2)

        val normalizedCalculation = calculation.coerceIn(0.0, 1.0)

        return EARTH_RADIUS_KM * 2 * atan2(
            sqrt(normalizedCalculation),
            sqrt(1 - normalizedCalculation),
        )
    }

    // Repeats either cache checking or the latest synchronization.
    fun retryInitialization() {
        val latitude = lastLatitude
        val longitude = lastLongitude

        if (latitude != null && longitude != null) {
            initializeApp(
                latitude = latitude,
                longitude = longitude,
                shouldCompareLocation = lastShouldCompareLocation,
            )
        } else {
            checkCacheBeforeLocation()
        }
    }

    // Removes an error after it has been delivered to the Snackbar host.
    fun onErrorShown() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }

    // Checks Room cache and synchronizes only data that is not available yet.
    private fun initializeApp(
        latitude: Double,
        longitude: Double,
        shouldCompareLocation: Boolean,
    ) {
        if (initializationJob?.isActive == true || _uiState.value.isFinished) return

        lastLatitude = latitude
        lastLongitude = longitude
        lastShouldCompareLocation = shouldCompareLocation

        _uiState.update { currentState ->
            currentState.copy(
                currentProgress = 0f,
                progressMessage = SplashProgressMessage.CheckingPrayerSchedule,
                isInitializing = true,
                showLocationDialog = false,
                locationCheckMode = null,
                result = null,
                errorMessage = null,
            )
        }

        startProgress()

        initializationJob = viewModelScope.launch {
            try {
                val currentDate = LocalDate.now()
                val formattedDate = currentDate.format(DATE_FORMATTER)
                var didSynchronize = false

                // Checks whether today's prayer schedule is already cached.
                val cachedPrayerSchedule = prayerRepository.getPrayerScheduleByDate(
                    date = formattedDate,
                )

                // Compares coordinates only when they come from the actual device location.
                val hasMovedFar = if (shouldCompareLocation) {
                    cachedPrayerSchedule?.let { cachedPrayer ->
                        calculateDistanceKm(
                            startLatitude = cachedPrayer.latitude,
                            startLongitude = cachedPrayer.longitude,
                            endLatitude = latitude,
                            endLongitude = longitude,
                        ) >= LOCATION_SYNC_DISTANCE_KM
                    } ?: false
                } else {
                    false
                }

                if (cachedPrayerSchedule == null || hasMovedFar) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            progressMessage = SplashProgressMessage.SynchronizingPrayerSchedule,
                        )
                    }

                    val calculationMethod = dataStoreManager.get(
                        key = PreferenceKeys.CALCULATION_METHOD,
                        defaultValue = DEFAULT_CALCULATION_METHOD,
                    ).first()
                    val madhab = dataStoreManager.get(
                        key = PreferenceKeys.MADZHAB,
                        defaultValue = DEFAULT_MADHAB,
                    ).first()

                    prayerRepository.syncPrayerSchedules(
                        year = currentDate.year,
                        month = currentDate.monthValue,
                        latitude = latitude,
                        longitude = longitude,
                        calculationMethod = calculationMethod,
                        madhab = madhab,
                    ).onSuccess {
                        didSynchronize = true
                    }.onError { error ->
                        finishWithError(error.message)
                        return@launch
                    }.onLoading {
                        finishWithError(
                            message = "Prayer schedule synchronization did not finish.",
                        )
                        return@launch
                    }
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        progressMessage = SplashProgressMessage.CheckingIslamicCalendar,
                    )
                }

                // Checks whether the current year's Islamic calendar is already cached.
                val hasIslamicCalendar = prayerRepository.hasIslamicCalendarForYear(
                    year = currentDate.year,
                )
                var calendarSyncFailed = false

                if (!hasIslamicCalendar) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            progressMessage = SplashProgressMessage.SynchronizingIslamicCalendar,
                        )
                    }

                    prayerRepository.syncIslamicCalendar(
                        year = currentDate.year,
                        latitude = latitude,
                        longitude = longitude,
                    ).onSuccess {
                        didSynchronize = true
                    }.onError {
                        calendarSyncFailed = true
                    }.onLoading {
                        calendarSyncFailed = true
                    }
                }

                val result = when {
                    calendarSyncFailed -> SplashResult.CalendarSyncFailed
                    didSynchronize -> SplashResult.SyncSuccess
                    else -> SplashResult.CacheReady
                }

                finishInitialization(result)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                Timber.e(error, "Failed to initialize application")
                finishWithError("Unable to prepare prayer schedule data.")
            }
        }
    }

    // Advances progress gradually and waits at 90% while synchronization is running.
    private fun startProgress() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            for (step in 1..9) {
                delay(PROGRESS_STEP_DELAY)

                _uiState.update { currentState ->
                    if (currentState.isInitializing) {
                        currentState.copy(
                            currentProgress = maxOf(
                                currentState.currentProgress,
                                step / 10f,
                            ),
                        )
                    } else {
                        currentState
                    }
                }
            }
        }
    }

    // Completes progress immediately when all required work has finished.
    private suspend fun finishInitialization(
        result: SplashResult,
    ) {
        progressJob?.cancel()

        try {
            // Ensures cached and newly synchronized schedules have active alarms.
            alarmScheduler.rescheduleAllAlarms()
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            Timber.e(error, "Failed to schedule reminders during startup")
        }

        _uiState.update { currentState ->
            currentState.copy(
                currentProgress = 1f,
                progressMessage = SplashProgressMessage.Finishing,
                isInitializing = false,
                result = result,
            )
        }

        delay(FINISH_DELAY)

        _uiState.update { currentState ->
            currentState.copy(isFinished = true)
        }
    }

    // Stops initialization and exposes an error for the reusable Snackbar.
    private fun finishWithError(message: String) {
        progressJob?.cancel()

        _uiState.update { currentState ->
            currentState.copy(
                isInitializing = false,
                errorMessage = message,
            )
        }
    }

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        private const val DEFAULT_LATITUDE = -6.2088
        private const val DEFAULT_LONGITUDE = 106.8456
        private const val DEFAULT_CALCULATION_METHOD = 20
        private const val DEFAULT_MADHAB = 0
        private const val PROGRESS_STEP_DELAY = 250L
        private const val FINISH_DELAY = 250L
        private const val LOCATION_SYNC_DISTANCE_KM = 30.0
        private const val EARTH_RADIUS_KM = 6371.0
    }
}

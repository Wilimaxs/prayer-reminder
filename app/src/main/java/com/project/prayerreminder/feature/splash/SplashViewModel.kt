package com.project.prayerreminder.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.prayerreminder.core.NetworkResult
import com.project.prayerreminder.core.data.local.pref.DataStoreManager
import com.project.prayerreminder.core.data.local.pref.PreferenceKeys
import com.project.prayerreminder.core.data.repository.PrayerRepository
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

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val prayerRepository: PrayerRepository,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState = _uiState.asStateFlow()

    private var initializationJob: Job? = null
    private var progressJob: Job? = null
    private var lastLatitude: Double? = null
    private var lastLongitude: Double? = null

    // Shows the fallback dialog when the device location cannot be used.
    fun onLocationUnavailable(
        settingsTarget: LocationSettingsTarget,
    ) {
        if (initializationJob?.isActive == true) return

        _uiState.update { currentState ->
            currentState.copy(
                showLocationDialog = true,
                locationSettingsTarget = settingsTarget,
                progressMessage = SplashProgressMessage.CheckingLocation,
            )
        }
    }

    // Hides the dialog while the user checks Android location settings.
    fun onLocationSettingsOpened() {
        _uiState.update { currentState ->
            currentState.copy(showLocationDialog = false)
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
        )
    }

    // Continues initialization with Jakarta when the location is unavailable.
    fun continueWithDefaultLocation() {
        initializeApp(
            latitude = DEFAULT_LATITUDE,
            longitude = DEFAULT_LONGITUDE,
        )
    }

    // Repeats the latest failed initialization when the error Snackbar is tapped.
    fun retryInitialization() {
        val latitude = lastLatitude ?: return
        val longitude = lastLongitude ?: return

        initializeApp(
            latitude = latitude,
            longitude = longitude,
        )
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
    ) {
        if (initializationJob?.isActive == true || _uiState.value.isFinished) return

        lastLatitude = latitude
        lastLongitude = longitude

        _uiState.update { currentState ->
            currentState.copy(
                currentProgress = 0f,
                progressMessage = SplashProgressMessage.CheckingPrayerSchedule,
                isInitializing = true,
                showLocationDialog = false,
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

                if (cachedPrayerSchedule == null) {
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

                    when (
                        val prayerResult = prayerRepository.syncPrayerSchedules(
                            year = currentDate.year,
                            month = currentDate.monthValue,
                            latitude = latitude,
                            longitude = longitude,
                            calculationMethod = calculationMethod,
                            madhab = madhab,
                        )
                    ) {
                        is NetworkResult.Success -> {
                            didSynchronize = true
                        }

                        is NetworkResult.Error -> {
                            finishWithError(prayerResult.message)
                            return@launch
                        }

                        NetworkResult.Loading -> {
                            finishWithError("Prayer schedule synchronization did not finish.")
                            return@launch
                        }
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

                    when (
                        prayerRepository.syncIslamicCalendar(
                            year = currentDate.year,
                            latitude = latitude,
                            longitude = longitude,
                        )
                    ) {
                        is NetworkResult.Success -> {
                            didSynchronize = true
                        }

                        is NetworkResult.Error,
                        NetworkResult.Loading -> {
                            calendarSyncFailed = true
                        }
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
    }
}

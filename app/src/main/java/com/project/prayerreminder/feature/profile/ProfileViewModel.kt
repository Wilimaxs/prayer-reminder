package com.project.prayerreminder.feature.profile

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.prayerreminder.core.alarm.AlarmScheduler
import com.project.prayerreminder.core.data.local.pref.DataStoreManager
import com.project.prayerreminder.core.data.local.pref.PreferenceKeys
import com.project.prayerreminder.core.data.repository.PrayerRepository
import com.project.prayerreminder.core.onError
import com.project.prayerreminder.core.onLoading
import com.project.prayerreminder.core.onSuccess
import com.project.prayerreminder.utils.enumeration.AppLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val prayerRepository: PrayerRepository,
    private val alarmScheduler: AlarmScheduler,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()
    private var prayerSettingSyncJob: Job? = null

    init {
        observeSettings()
    }

    private fun observeSettings() {
        val defaultPrayerSettings = ProfilePrayerSettingsUiState()
        val defaultNotificationSettings = ProfileNotificationSettingsUiState()
        val defaultAppearanceSettings = ProfileAppearanceSettingsUiState()

        viewModelScope.launch {
            combine(
                dataStoreManager.get(
                    key = PreferenceKeys.CALCULATION_METHOD,
                    defaultValue = defaultPrayerSettings.calculationMethod.apiCode,
                ),
                dataStoreManager.get(
                    key = PreferenceKeys.MADZHAB,
                    defaultValue = defaultPrayerSettings.madhab.apiCode,
                ),
                dataStoreManager.get(
                    key = PreferenceKeys.PRAYER_REMINDERS_ENABLED,
                    defaultValue = defaultNotificationSettings.isPrayerRemindersEnabled,
                ),
                dataStoreManager.get(
                    key = PreferenceKeys.REMINDER_OFFSET_MINUTES,
                    defaultValue = defaultNotificationSettings.reminderOffsetMinutes,
                ),
                dataStoreManager.get(
                    key = PreferenceKeys.APP_LANGUAGE,
                    defaultValue = defaultAppearanceSettings.language.languageTag,
                ),
            ) {
                    calculationMethodCode,
                    madhabCode,
                    isPrayerReminderEnabled,
                    reminderOffsetMinutes,
                    languageTag,
                ->

                val prayerSettings = ProfilePrayerSettingsUiState(
                    calculationMethod = PrayerCalculationMethod.fromApiCode(
                        calculationMethodCode,
                    ),
                    madhab = AsrMadhab.fromApiCode(
                        madhabCode,
                    ),
                )

                val notificationSettings = ProfileNotificationSettingsUiState(
                    isPrayerRemindersEnabled = isPrayerReminderEnabled,
                    reminderOffsetMinutes = reminderOffsetMinutes,
                )

                val appearanceSettings = ProfileAppearanceSettingsUiState(
                    language = AppLanguage.fromLanguageTag(languageTag),
                )

                Triple(
                    prayerSettings,
                    notificationSettings,
                    appearanceSettings,
                )
            }
                .catch { exception ->
                    Timber.e(exception)

                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            message = ProfileMessage.LoadSettingsFailed,
                        )
                    }
                }
                .collect { (prayerSettings, notificationSettings, appearanceSettings) ->
                    // Updates both Profile setting groups from the latest preferences.
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            prayerSettings = prayerSettings,
                            notificationSettings = notificationSettings,
                            appearanceSettings = appearanceSettings,
                        )
                    }
                }
        }
    }

    fun updatePrayerReminders(isEnabled: Boolean) {
        savePreference(
            key = PreferenceKeys.PRAYER_REMINDERS_ENABLED,
            value = isEnabled,
            onSaved = {
                alarmScheduler.reschedulePrayerAlarms()
            },
        )
    }

    fun openBottomSheet(
        type: ProfileBottomSheetType,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                bottomSheet = when (type) {
                    ProfileBottomSheetType.CalculationMethod -> {
                        currentState.bottomSheet.copy(
                            activeBottomSheet = type,
                            selectedCalculationMethod = currentState.prayerSettings.calculationMethod,
                        )
                    }

                    ProfileBottomSheetType.Madhab -> {
                        currentState.bottomSheet.copy(
                            activeBottomSheet = type,
                            selectedMadhab = currentState.prayerSettings.madhab,
                        )
                    }

                    ProfileBottomSheetType.ReminderOffset -> {
                        currentState.bottomSheet.copy(
                            activeBottomSheet = type,
                            selectedReminderOffsetMinutes = currentState.notificationSettings.reminderOffsetMinutes,
                        )
                    }

                    ProfileBottomSheetType.Language -> {
                        currentState.bottomSheet.copy(
                            activeBottomSheet = type,
                            selectedLanguage =
                                currentState.appearanceSettings.language,
                        )
                    }
                },
            )
        }
    }

    fun selectCalculationMethod(
        method: PrayerCalculationMethod,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                bottomSheet = currentState.bottomSheet.copy(
                    selectedCalculationMethod = method,
                ),
            )
        }
    }

    fun selectMadhab(
        madhab: AsrMadhab,
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                bottomSheet = currentState.bottomSheet.copy(
                    selectedMadhab = madhab,
                ),
            )
        }
    }

    fun selectReminderOffset(minutes: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                bottomSheet = currentState.bottomSheet.copy(
                    selectedReminderOffsetMinutes = minutes,
                ),
            )
        }
    }

    fun selectLanguage(language: AppLanguage) {
        _uiState.update { currentState ->
            currentState.copy(
                bottomSheet = currentState.bottomSheet.copy(
                    selectedLanguage = language,
                ),
            )
        }
    }

    fun confirmBottomSheet() {
        val currentState = _uiState.value

        when (currentState.bottomSheet.activeBottomSheet) {
            ProfileBottomSheetType.CalculationMethod -> {
                val selectedMethod = currentState.bottomSheet.selectedCalculationMethod

                // Closes the sheet without using the limit when the value did not change.
                if (selectedMethod == currentState.prayerSettings.calculationMethod) {
                    dismissBottomSheet()
                    return
                }

                synchronizePrayerSetting(
                    calculationMethod = selectedMethod,
                    madhab = currentState.prayerSettings.madhab,
                    preferenceKey = PreferenceKeys.CALCULATION_METHOD,
                    preferenceValue = selectedMethod.apiCode,
                )
                return
            }

            ProfileBottomSheetType.Madhab -> {
                val selectedMadhab = currentState.bottomSheet.selectedMadhab

                // Closes the sheet without using the limit when the value did not change.
                if (selectedMadhab == currentState.prayerSettings.madhab) {
                    dismissBottomSheet()
                    return
                }

                synchronizePrayerSetting(
                    calculationMethod = currentState.prayerSettings.calculationMethod,
                    madhab = selectedMadhab,
                    preferenceKey = PreferenceKeys.MADZHAB,
                    preferenceValue = selectedMadhab.apiCode,
                )
                return
            }

            ProfileBottomSheetType.ReminderOffset -> {
                savePreference(
                    key = PreferenceKeys.REMINDER_OFFSET_MINUTES,
                    value = currentState.bottomSheet.selectedReminderOffsetMinutes,
                    onSaved = {
                        alarmScheduler.reschedulePrayerAlarms()
                    },
                )
            }

            ProfileBottomSheetType.Language -> {
                savePreference(
                    key = PreferenceKeys.APP_LANGUAGE,
                    value = currentState.bottomSheet.selectedLanguage.languageTag,
                )
            }

            null -> return
        }

        dismissBottomSheet()
    }

    // Applies the new prayer setting only after the monthly prayer data is synchronized.
    private fun synchronizePrayerSetting(
        calculationMethod: PrayerCalculationMethod,
        madhab: AsrMadhab,
        preferenceKey: Preferences.Key<Int>,
        preferenceValue: Int,
    ) {
        if (prayerSettingSyncJob?.isActive == true) return

        prayerSettingSyncJob = viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isPrayerSettingSyncing = true)
            }

            try {
                val currentTime = System.currentTimeMillis()
                val storedCount = dataStoreManager.get(
                    key = PreferenceKeys.PRAYER_SETTING_CHANGE_COUNT,
                    defaultValue = 0,
                ).first()
                val storedWindowStartedAt = dataStoreManager.get(
                    key = PreferenceKeys.PRAYER_SETTING_CHANGE_WINDOW_STARTED_AT,
                    defaultValue = 0L,
                ).first()
                val isCurrentWindow = storedWindowStartedAt > 0L &&
                        currentTime - storedWindowStartedAt < CHANGE_LIMIT_WINDOW_MILLIS
                val currentCount = if (isCurrentWindow) storedCount else 0
                val windowStartedAt = if (isCurrentWindow) {
                    storedWindowStartedAt
                } else {
                    currentTime
                }

                // Stops synchronization after three successful changes in the current hour.
                if (currentCount >= MAX_PRAYER_SETTING_CHANGES) {
                    val remainingMillis = CHANGE_LIMIT_WINDOW_MILLIS -
                            (currentTime - windowStartedAt)
                    val remainingMinutes = (
                            remainingMillis + MILLIS_PER_MINUTE - 1
                            ) / MILLIS_PER_MINUTE

                    _uiState.update { currentState ->
                        currentState.copy(
                            isPrayerSettingSyncing = false,
                            bottomSheet = currentState.bottomSheet.copy(
                                activeBottomSheet = null,
                            ),
                            changeLimitRemainingMinutes = remainingMinutes.coerceAtLeast(1L),
                        )
                    }
                    return@launch
                }

                val currentDate = LocalDate.now()
                val cachedPrayer = prayerRepository.getPrayerScheduleByDate(
                    date = currentDate.format(DATE_FORMATTER),
                )

                if (cachedPrayer == null) {
                    finishPrayerSettingSync(ProfileMessage.PrayerSettingSyncFailed)
                    return@launch
                }

                var isSyncSuccessful = false

                // Uses the coordinates already stored in Room without requesting device location.
                prayerRepository.syncPrayerSchedules(
                    year = currentDate.year,
                    month = currentDate.monthValue,
                    latitude = cachedPrayer.latitude,
                    longitude = cachedPrayer.longitude,
                    calculationMethod = calculationMethod.apiCode,
                    madhab = madhab.apiCode,
                ).onSuccess {
                    isSyncSuccessful = true
                }.onError { error ->
                    Timber.e("Failed to synchronize prayer setting: ${error.message}")
                }.onLoading {
                    Timber.e("Prayer setting synchronization did not finish")
                }

                if (!isSyncSuccessful) {
                    finishPrayerSettingSync(ProfileMessage.PrayerSettingSyncFailed)
                    return@launch
                }

                // Counts only a setting change whose API and Room synchronization succeeded.
                dataStoreManager.save(
                    key = preferenceKey,
                    value = preferenceValue,
                )
                dataStoreManager.save(
                    key = PreferenceKeys.PRAYER_SETTING_CHANGE_WINDOW_STARTED_AT,
                    value = windowStartedAt,
                )
                dataStoreManager.save(
                    key = PreferenceKeys.PRAYER_SETTING_CHANGE_COUNT,
                    value = currentCount + 1,
                )

                // Replaces existing alarms using the newly downloaded prayer times.
                alarmScheduler.reschedulePrayerAlarms()
                finishPrayerSettingSync(ProfileMessage.PrayerSettingSyncSuccess)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Timber.e(exception, "Failed to update prayer setting")
                finishPrayerSettingSync(ProfileMessage.PrayerSettingSyncFailed)
            }
        }
    }

    // Closes the selection sheet and exposes the synchronization result to the Snackbar.
    private fun finishPrayerSettingSync(message: ProfileMessage) {
        _uiState.update { currentState ->
            currentState.copy(
                isPrayerSettingSyncing = false,
                bottomSheet = currentState.bottomSheet.copy(
                    activeBottomSheet = null,
                ),
                message = message,
            )
        }
    }

    // Removes a message after it has been displayed by the Snackbar host.
    fun clearMessage() {
        _uiState.update { currentState ->
            currentState.copy(message = null)
        }
    }

    // Closes the change-limit dialog.
    fun dismissChangeLimitDialog() {
        _uiState.update { currentState ->
            currentState.copy(changeLimitRemainingMinutes = null)
        }
    }

    fun dismissBottomSheet() {
        _uiState.update { currentState ->
            currentState.copy(
                bottomSheet = currentState.bottomSheet.copy(
                    activeBottomSheet = null,
                ),
            )
        }
    }

    private fun <T> savePreference(
        key: Preferences.Key<T>,
        value: T,
        onSaved: suspend () -> Unit = {},
    ) {
        viewModelScope.launch {
            try {
                dataStoreManager.save(
                    key = key,
                    value = value,
                )
                onSaved()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Timber.e(exception)
                _uiState.update { currentState ->
                    currentState.copy(
                        message = ProfileMessage.SaveSettingFailed,
                    )
                }
            }
        }
    }

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        private const val MAX_PRAYER_SETTING_CHANGES = 3
        private const val MILLIS_PER_MINUTE = 60_000L
        private const val CHANGE_LIMIT_WINDOW_MILLIS = 60 * MILLIS_PER_MINUTE
    }
}

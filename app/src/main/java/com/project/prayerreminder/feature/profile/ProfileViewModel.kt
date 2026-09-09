package com.project.prayerreminder.feature.profile

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.prayerreminder.core.data.local.pref.DataStoreManager
import com.project.prayerreminder.core.data.local.pref.PreferenceKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        val defaultPrayerSettings = ProfilePrayerSettingsUiState()
        val defaultNotificationSettings = ProfileNotificationSettingsUiState()

        viewModelScope.launch {
            // Combines all persisted Profile settings into one reactive stream.
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
            ) {
                    calculationMethodCode,
                    madhabCode,
                    isPrayerReminderEnabled,
                    reminderOffsetMinutes,
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

                prayerSettings to notificationSettings
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
                .collect { (prayerSettings, notificationSettings) ->
                    // Updates both Profile setting groups from the latest preferences.
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            prayerSettings = prayerSettings,
                            notificationSettings = notificationSettings,
                        )
                    }
                }
        }
    }

    fun updatePrayerReminders(isEnabled: Boolean) {
        savePreference(
            key = PreferenceKeys.PRAYER_REMINDERS_ENABLED,
            value = isEnabled,
        )
    }

    fun clearMessage() {
        _uiState.update { currentState ->
            currentState.copy(
                message = null,
            )
        }
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

    fun confirmBottomSheet() {
        val currentState = _uiState.value

        when (currentState.bottomSheet.activeBottomSheet) {
            ProfileBottomSheetType.CalculationMethod -> {
                savePreference(
                    key = PreferenceKeys.CALCULATION_METHOD,
                    value = currentState.bottomSheet.selectedCalculationMethod.apiCode,
                )
            }

            ProfileBottomSheetType.Madhab -> {
                savePreference(
                    key = PreferenceKeys.MADZHAB,
                    value = currentState
                        .bottomSheet
                        .selectedMadhab
                        .apiCode,
                )
            }

            ProfileBottomSheetType.ReminderOffset -> {
                savePreference(
                    key = PreferenceKeys.REMINDER_OFFSET_MINUTES,
                    value = currentState.bottomSheet.selectedReminderOffsetMinutes,
                )
            }

            null -> return
        }

        dismissBottomSheet()
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
    ) {
        viewModelScope.launch {
            try {
                dataStoreManager.save(
                    key = key,
                    value = value,
                )
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
}
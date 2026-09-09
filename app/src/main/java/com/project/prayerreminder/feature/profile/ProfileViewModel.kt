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
        observePrayerSettings()
    }

    private fun observePrayerSettings() {
        val defaultSettings = ProfilePrayerSettingsUiState()

        viewModelScope.launch {
            combine(
                dataStoreManager.get(
                    key = PreferenceKeys.CITY_NAME,
                    defaultValue = defaultSettings.cityName,
                ),
                dataStoreManager.get(
                    key = PreferenceKeys.AUTO_LOCATION,
                    defaultValue = defaultSettings.isAutoLocationEnabled,
                ),
                dataStoreManager.get(
                    key = PreferenceKeys.CALCULATION_METHOD,
                    defaultValue = defaultSettings.calculationMethod.apiCode,
                ),
                dataStoreManager.get(
                    key = PreferenceKeys.MADZHAB,
                    defaultValue = defaultSettings.madhab.apiCode,
                ),
            ) { cityName, isAutoLocationEnabled, calculationMethodCode, madhabCode ->

                ProfilePrayerSettingsUiState(
                    cityName = cityName,
                    isAutoLocationEnabled = isAutoLocationEnabled,
                    calculationMethod = PrayerCalculationMethod.fromApiCode(calculationMethodCode),
                    madhab = AsrMadhab.fromApiCode(madhabCode),
                )
            }
                .catch {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            message = ProfileMessage.LoadSettingsFailed,
                        )
                    }
                }
                .collect { prayerSettings ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            prayerSettings = prayerSettings,
                        )
                    }
                }
        }
    }

    fun updateAutoLocation(isEnabled: Boolean) {
        savePreference(
            key = PreferenceKeys.AUTO_LOCATION,
            value = isEnabled,
        )
    }

    fun updateCityName(cityName: String) {
        savePreference(
            key = PreferenceKeys.CITY_NAME,
            value = cityName,
        )
    }

    fun updateCalculationMethod(method: PrayerCalculationMethod) {
        savePreference(
            key = PreferenceKeys.CALCULATION_METHOD,
            value = method.apiCode,
        )
    }

    fun updatePrayerReminders(isEnabled: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                notificationSettings =
                    currentState.notificationSettings.copy(
                        isPrayerRemindersEnabled = isEnabled,
                    ),
            )
        }
    }

    fun updateMadhab(madhab: AsrMadhab) {
        savePreference(
            key = PreferenceKeys.MADZHAB,
            value = madhab.apiCode,
        )
    }

    fun clearMessage() {
        _uiState.update { currentState ->
            currentState.copy(
                message = null,
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
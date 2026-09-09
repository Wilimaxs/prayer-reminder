package com.project.prayerreminder.feature.profile

import com.project.prayerreminder.utils.enumeration.AppLanguage

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: ProfileUserUiState = ProfileUserUiState(),
    val prayerSettings: ProfilePrayerSettingsUiState = ProfilePrayerSettingsUiState(),
    val notificationSettings: ProfileNotificationSettingsUiState = ProfileNotificationSettingsUiState(),
    val appearanceSettings: ProfileAppearanceSettingsUiState = ProfileAppearanceSettingsUiState(),
    val bottomSheet: ProfileBottomSheetUiState = ProfileBottomSheetUiState(),
    val message: ProfileMessage? = null,
)

data class ProfileUserUiState(
    val name: String = "Hamba Tuhan",
    val avatarPath: String? = null,
)

data class ProfilePrayerSettingsUiState(
    val calculationMethod: PrayerCalculationMethod = PrayerCalculationMethod.KemenagIndonesia,
    val madhab: AsrMadhab = AsrMadhab.Shafi,
)

data class ProfileNotificationSettingsUiState(
    val isPrayerRemindersEnabled: Boolean = true,
    val reminderOffsetMinutes: Int = 10,
)

data class ProfileAppearanceSettingsUiState(
    val language: AppLanguage = AppLanguage.English,
)

// For BottomSheet
data class ProfileBottomSheetUiState(
    val activeBottomSheet: ProfileBottomSheetType? = null,
    val selectedCalculationMethod: PrayerCalculationMethod = PrayerCalculationMethod.KemenagIndonesia,
    val selectedMadhab: AsrMadhab = AsrMadhab.Shafi,
    val selectedReminderOffsetMinutes: Int = 10,
    val selectedLanguage: AppLanguage = AppLanguage.English,
)

enum class ProfileBottomSheetType {
    CalculationMethod,
    Madhab,
    ReminderOffset,
    Language,
}

enum class PrayerCalculationMethod(
    val apiCode: Int,
) {
    KemenagIndonesia(apiCode = 20),
    MuslimWorldLeague(apiCode = 3),
    JakimMalaysia(apiCode = 17);

    companion object {
        fun fromApiCode(
            apiCode: Int,
        ): PrayerCalculationMethod {
            return entries.firstOrNull { method ->
                method.apiCode == apiCode
            } ?: KemenagIndonesia
        }
    }
}

enum class AsrMadhab(
    val apiCode: Int,
) {
    Shafi(apiCode = 0),
    Hanafi(apiCode = 1);

    companion object {
        fun fromApiCode(
            apiCode: Int,
        ): AsrMadhab {
            return entries.firstOrNull { madhab ->
                madhab.apiCode == apiCode
            } ?: Shafi
        }
    }
}

enum class ProfileMessage {
    LoadSettingsFailed,
    SaveSettingFailed,
}
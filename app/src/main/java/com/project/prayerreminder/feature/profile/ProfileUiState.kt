package com.project.prayerreminder.feature.profile

data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: ProfileUserUiState = ProfileUserUiState(),
    val prayerSettings: ProfilePrayerSettingsUiState = ProfilePrayerSettingsUiState(),
    val message: ProfileMessage? = null,
)

data class ProfileUserUiState(
    val name: String = "Hamba Tuhan",
    val avatarPath: String? = null,
)

data class ProfilePrayerSettingsUiState(
    val cityName: String = "Bandung, Indonesia",
    val isAutoLocationEnabled: Boolean = false,
    val calculationMethod: PrayerCalculationMethod = PrayerCalculationMethod.KemenagIndonesia,
    val madhab: AsrMadhab = AsrMadhab.Shafi,
)

enum class PrayerCalculationMethod(
    val apiCode: Int,
) {
    KemenagIndonesia(apiCode = 20);

    companion object {
        fun fromApiCode(apiCode: Int): PrayerCalculationMethod {
            return entries.firstOrNull {
                it.apiCode == apiCode
            } ?: KemenagIndonesia
        }
    }
}

enum class AsrMadhab(
    val apiCode: Int,
    val storageValue: String,
) {
    Shafi(
        apiCode = 0,
        storageValue = "shafi",
    ),
    Hanafi(
        apiCode = 1,
        storageValue = "hanafi",
    );

    companion object {
        fun fromStoredValue(value: String): AsrMadhab {
            return entries.firstOrNull {
                it.storageValue == value
            } ?: Shafi
        }
    }
}

enum class ProfileMessage {
    LoadSettingsFailed,
    SaveSettingFailed,
}
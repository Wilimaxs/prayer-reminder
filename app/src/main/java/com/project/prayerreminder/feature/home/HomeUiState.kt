package com.project.prayerreminder.feature.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.project.prayerreminder.R

data class HomeUiState(
    val isLoading: Boolean = true,
    val content: HomeContentUiState? = null,
    val errorMessage: String? = null,
)

data class HomeContentUiState(
    val locationName: String,
    val gregorianDate: String,
    val hijriDate: String,
    val nextPrayer: HomeNextPrayerUiState?,
    val prayers: List<HomePrayerUiState>,
)

data class HomeNextPrayerUiState(
    val prayer: HomePrayer,
    val time: String,
    val remainingMinutes: Long,
)

data class HomePrayerUiState(
    val prayer: HomePrayer,
    val time: String,
    val isActive: Boolean,
)

enum class HomePrayer(
    @param:StringRes val nameRes: Int,
    @param:DrawableRes val iconRes: Int,
) {
    Fajr(
        nameRes = R.string.fajr,
        iconRes = R.drawable.ic_fajr,
    ),
    Dhuhr(
        nameRes = R.string.dhuhr,
        iconRes = R.drawable.ic_dhuhr,
    ),
    Asr(
        nameRes = R.string.asr,
        iconRes = R.drawable.ic_asr,
    ),
    Maghrib(
        nameRes = R.string.maghrib,
        iconRes = R.drawable.ic_maghrib,
    ),
    Isha(
        nameRes = R.string.isha,
        iconRes = R.drawable.ic_isha,
    ),
}

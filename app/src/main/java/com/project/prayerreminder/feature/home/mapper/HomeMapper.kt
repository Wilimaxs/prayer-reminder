package com.project.prayerreminder.feature.home.mapper

import com.project.prayerreminder.core.data.local.entity.PrayerEntity
import com.project.prayerreminder.feature.home.HomeContentUiState
import com.project.prayerreminder.feature.home.HomeNextPrayerUiState
import com.project.prayerreminder.feature.home.HomePrayer
import com.project.prayerreminder.feature.home.HomePrayerUiState
import com.project.prayerreminder.utils.enumeration.AppLanguage
import com.project.prayerreminder.utils.extensions.formatHijriDate
import com.project.prayerreminder.utils.extensions.toLocalizedGregorianDate
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// Maps cached prayer data into values that can be displayed directly by Home.
fun PrayerEntity.toHomeContentUiState(
    nextDaySchedule: PrayerEntity?,
    currentDateTime: LocalDateTime,
    locationName: String,
    language: AppLanguage,
): HomeContentUiState {
    val prayerTimes = toPrayerUiStates(currentDateTime.toLocalTime())
    val nextPrayer = findNextPrayer(
        prayerTimes = prayerTimes,
        nextDaySchedule = nextDaySchedule,
        currentDateTime = currentDateTime,
    )

    return HomeContentUiState(
        locationName = locationName,
        gregorianDate = date.toLocalizedGregorianDate(language),
        hijriDate = formatHijriDate(
            day = hijriDay,
            month = hijriMonth,
            year = hijriYear,
            language = language,
        ),
        nextPrayer = nextPrayer,
        prayers = prayerTimes,
    )
}

// Builds the five ordered prayer items and marks the current prayer period.
private fun PrayerEntity.toPrayerUiStates(
    currentTime: LocalTime,
): List<HomePrayerUiState> {
    val prayerTimes = listOf(
        HomePrayer.Fajr to fajr,
        HomePrayer.Dhuhr to dhuhr,
        HomePrayer.Asr to asr,
        HomePrayer.Maghrib to maghrib,
        HomePrayer.Isha to isha,
    )
    val latestPrayerIndex = prayerTimes.indexOfLast { (_, time) ->
        parsePrayerTime(time)?.let { prayerTime ->
            !currentTime.isBefore(prayerTime)
        } ?: false
    }
    // Before Fajr, the active prayer period is still Isha from the previous day.
    val activePrayerIndex = latestPrayerIndex.takeIf { it >= 0 }
        ?: prayerTimes.lastIndex

    return prayerTimes.mapIndexed { index, (prayer, time) ->
        HomePrayerUiState(
            prayer = prayer,
            time = time,
            isActive = index == activePrayerIndex,
        )
    }
}

// Finds the next prayer today or tomorrow and calculates its remaining minutes.
private fun findNextPrayer(
    prayerTimes: List<HomePrayerUiState>,
    nextDaySchedule: PrayerEntity?,
    currentDateTime: LocalDateTime,
): HomeNextPrayerUiState? {
    val nextPrayerToday = prayerTimes.firstOrNull { prayer ->
        parsePrayerTime(prayer.time)?.let { prayerTime ->
            currentDateTime.toLocalTime().isBefore(prayerTime)
        } ?: false
    }

    val nextPrayer = nextPrayerToday ?: nextDaySchedule?.let { schedule ->
        HomePrayerUiState(
            prayer = HomePrayer.Fajr,
            time = schedule.fajr,
            isActive = false,
        )
    } ?: return null

    val prayerTime = parsePrayerTime(nextPrayer.time) ?: return null
    val prayerDate = if (nextPrayerToday != null) {
        currentDateTime.toLocalDate()
    } else {
        currentDateTime.toLocalDate().plusDays(1)
    }
    val prayerDateTime = prayerDate.atTime(prayerTime)
    val remainingSeconds = Duration.between(
        currentDateTime,
        prayerDateTime,
    ).seconds.coerceAtLeast(0L)

    return HomeNextPrayerUiState(
        prayer = nextPrayer.prayer,
        time = nextPrayer.time,
        remainingMinutes = (remainingSeconds + 59L) / 60L,
    )
}

// Parses the normalized "HH:mm" value stored in Room.
private fun parsePrayerTime(
    value: String,
): LocalTime? {
    return runCatching {
        LocalTime.parse(value, PRAYER_TIME_FORMATTER)
    }.getOrNull()
}

private val PRAYER_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")

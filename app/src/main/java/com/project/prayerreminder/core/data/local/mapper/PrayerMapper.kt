package com.project.prayerreminder.core.data.local.mapper

import com.project.prayerreminder.core.data.local.entity.PrayerEntity
import com.project.prayerreminder.core.data.remote.model.PrayerDataDto
import com.project.prayerreminder.utils.extensions.toDatePartsOrNull

fun PrayerDataDto.toEntity(
    latitude: Double,
    longitude: Double,
): PrayerEntity? {
    val dateInfo = date ?: return null
    val timingsDto = timings ?: return null

    val gregorianDate = dateInfo.gregorian
        ?.date
        ?.takeIf { it.isNotBlank() }
        ?: return null

    // Splits the raw Hijri date into day, month, and year.
    val hijriDate = dateInfo.hijri
        ?.date
        ?.toDatePartsOrNull()
        ?: return null

    // Removes timezone information such as "(WIB)" from prayer time.
    fun cleanTime(time: String?): String? {
        return time
            ?.substringBefore(" ")
            ?.trim()
            ?.takeIf { it.isNotBlank() }
    }

    // Ensures incomplete prayer schedules are not stored in the Room.
    val fajr = cleanTime(timingsDto.fajr) ?: return null
    val dhuhr = cleanTime(timingsDto.dhuhr) ?: return null
    val asr = cleanTime(timingsDto.asr) ?: return null
    val maghrib = cleanTime(timingsDto.maghrib) ?: return null
    val isha = cleanTime(timingsDto.isha) ?: return null

    return PrayerEntity(
        date = gregorianDate,
        fajr = fajr,
        dhuhr = dhuhr,
        asr = asr,
        maghrib = maghrib,
        isha = isha,
        hijriDay = hijriDate.day,
        hijriMonth = hijriDate.month,
        hijriYear = hijriDate.year,
        latitude = latitude,
        longitude = longitude,
    )
}
package com.project.prayerreminder.core.data.local.mapper

import com.project.prayerreminder.core.data.local.entity.PrayerEntity
import com.project.prayerreminder.core.data.remote.model.PrayerDataDto

fun PrayerDataDto.toEntity(
    latitude: Double,
    longitude: Double,
): PrayerEntity? {
    val dateStr = date?.gregorian?.date ?: return null
    val timingsDto = timings ?: return null

    // Removes timezone information from prayer time values.
    fun cleanTime(time: String?): String {
        return time
            ?.split(" ")
            ?.firstOrNull()
            .orEmpty()
    }

    return PrayerEntity(
        date = dateStr,
        fajr = cleanTime(timingsDto.fajr),
        sunrise = cleanTime(timingsDto.sunrise),
        dhuhr = cleanTime(timingsDto.dhuhr),
        asr = cleanTime(timingsDto.asr),
        maghrib = cleanTime(timingsDto.maghrib),
        isha = cleanTime(timingsDto.isha),
        imsak = cleanTime(timingsDto.imsak),
        readableDate = date.readable.orEmpty(),
        hijriDate = date.hijri?.date.orEmpty(),
        hijriDay = date.hijri?.day.orEmpty(),
        hijriMonthEn = date.hijri?.month?.en.orEmpty(),
        hijriMonthAr = date.hijri?.month?.ar.orEmpty(),
        hijriYear = date.hijri?.year.orEmpty(),
        hijriHolidays = date.hijri?.holidays.orEmpty(),
        dayNameEn = date.gregorian.weekday?.en.orEmpty(),
        latitude = latitude,
        longitude = longitude,
    )
}
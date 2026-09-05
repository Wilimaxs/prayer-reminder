package com.project.prayerreminder.core.data.local.mapper

import com.project.prayerreminder.core.data.local.entity.PrayerEntity
import com.project.prayerreminder.core.data.remote.model.PrayerDataDto

fun PrayerDataDto.toEntity(latitude: Double, longitude: Double): PrayerEntity? {
    val dateStr = this.date?.gregorian?.date ?: return null
    val timingsDto = this.timings ?: return null

    fun cleanTime(time: String?): String {
        return time?.split(" ")?.firstOrNull() ?: ""
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
        readableDate = this.date.readable.orEmpty(),
        hijriDate = this.date.hijri?.date.orEmpty(),
        hijriDay = this.date.hijri?.day.orEmpty(),
        hijriMonthEn = this.date.hijri?.month?.en.orEmpty(),
        hijriMonthAr = this.date.hijri?.month?.ar.orEmpty(),
        hijriYear = this.date.hijri?.year.orEmpty(),
        dayNameEn = this.date.gregorian.weekday?.en.orEmpty(),
        latitude = latitude,
        longitude = longitude
    )
}
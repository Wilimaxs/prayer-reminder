package com.project.prayerreminder.core.data.remote.model

import com.google.gson.annotations.SerializedName

data class PrayerDataDto(
    @SerializedName("timings")
    val timings: TimingsDto?,
    @SerializedName("date")
    val date: DateInfoDto?
) {
    data class TimingsDto(
        @SerializedName("Fajr")
        val fajr: String?,
        @SerializedName("Sunrise")
        val sunrise: String?,
        @SerializedName("Dhuhr")
        val dhuhr: String?,
        @SerializedName("Asr")
        val asr: String?,
        @SerializedName("Sunset")
        val sunset: String?,
        @SerializedName("Maghrib")
        val maghrib: String?,
        @SerializedName("Isha")
        val isha: String?,
        @SerializedName("Imsak")
        val imsak: String?,
        @SerializedName("Midnight")
        val midnight: String?
    )

    data class DateInfoDto(
        @SerializedName("readable")
        val readable: String?,
        @SerializedName("timestamp")
        val timestamp: String?,
        @SerializedName("gregorian")
        val gregorian: GregorianDateDto?,
        @SerializedName("hijri")
        val hijri: HijriDateDto?
    ) {
        data class GregorianDateDto(
            @SerializedName("date")
            val date: String?,
            @SerializedName("day")
            val day: String?,
            @SerializedName("weekday")
            val weekday: WeekdayDto?,
            @SerializedName("month")
            val month: MonthDto?,
            @SerializedName("year")
            val year: String?
        )

        data class HijriDateDto(
            @SerializedName("date")
            val date: String?,
            @SerializedName("day")
            val day: String?,
            @SerializedName("weekday")
            val weekday: WeekdayDto?,
            @SerializedName("month")
            val month: MonthDto?,
            @SerializedName("year")
            val year: String?,
            @SerializedName("holidays")
            val holidays: List<String>?
        )

        data class WeekdayDto(
            @SerializedName("en")
            val en: String?,
            @SerializedName("ar")
            val ar: String?
        )

        data class MonthDto(
            @SerializedName("number")
            val number: Int?,
            @SerializedName("en")
            val en: String?,
            @SerializedName("ar")
            val ar: String?,
            @SerializedName("days")
            val days: Int?
        )
    }
}
package com.project.prayerreminder.core.data.remote.model

import com.google.gson.annotations.SerializedName

data class CalendarDataDto(
    @SerializedName("date")
    val date: DateInfoDto?
) {
    data class DateInfoDto(
        @SerializedName("gregorian")
        val gregorian: GregorianDateDto?,
        @SerializedName("hijri")
        val hijri: HijriDateDto?
    ) {
        data class GregorianDateDto(
            @SerializedName("date")
            val date: String?,
        )
        data class HijriDateDto(
            @SerializedName("date")
            val date: String?,
            @SerializedName("holidays")
            val holidays: List<String>?,
        )
    }
}
package com.project.prayerreminder.core.data.remote

import com.project.prayerreminder.core.data.remote.model.BaseResponse
import com.project.prayerreminder.core.data.remote.model.CalendarDataDto
import com.project.prayerreminder.core.data.remote.model.PrayerDataDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Retrieves one month of prayer schedules.
    @GET("calendar/{year}/{month}")
    suspend fun getPrayerCalendarByGregorianMonth(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("method") method: Int,
        @Query("school") school: Int,
    ): Response<BaseResponse<List<PrayerDataDto>>>

    // Retrieves one Gregorian year grouped by month.
    @GET("calendar/{year}")
    suspend fun getIslamicCalendarByGregorianYear(
        @Path("year") year: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ): Response<BaseResponse<Map<String, List<CalendarDataDto>>>>
}
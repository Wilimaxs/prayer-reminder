package com.project.prayerreminder.core.data.remote

import com.project.prayerreminder.core.data.remote.model.BaseResponse
import com.project.prayerreminder.core.data.remote.model.PrayerDataDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("calendar/{year}/{month}")
    suspend fun getPrayerCalendarByGregorian(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("method") method: Int = 20
    ): Response<BaseResponse<List<PrayerDataDto>>>

    @GET("hijriCalendar/{year}/{month}")
    suspend fun getPrayerCalendarByHijri(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("method") method: Int = 20
    ): Response<BaseResponse<List<PrayerDataDto>>>
}
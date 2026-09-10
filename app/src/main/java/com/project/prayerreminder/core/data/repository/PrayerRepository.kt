package com.project.prayerreminder.core.data.repository

import com.project.prayerreminder.core.NetworkResult
import com.project.prayerreminder.core.data.local.dao.IslamicCalendarDao
import com.project.prayerreminder.core.data.local.dao.PrayerDao
import com.project.prayerreminder.core.data.local.entity.IslamicCalendarEntity
import com.project.prayerreminder.core.data.local.entity.PrayerEntity
import com.project.prayerreminder.core.data.local.mapper.toEntity
import com.project.prayerreminder.core.data.local.mapper.toIslamicCalendarEntity
import com.project.prayerreminder.core.data.local.pref.DataStoreManager
import com.project.prayerreminder.core.data.local.pref.PreferenceKeys
import com.project.prayerreminder.core.data.remote.ApiService
import com.project.prayerreminder.core.safeApiCall
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class PrayerRepository @Inject constructor(
    private val apiService: ApiService,
    private val prayerDao: PrayerDao,
    private val islamicCalendarDao: IslamicCalendarDao,
    private val dataStoreManager: DataStoreManager,
) {

    // Downloads one month of prayer schedules and replaces the previous Room cache.
    suspend fun syncPrayerSchedules(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double,
        calculationMethod: Int,
        madhab: Int,
    ): NetworkResult<Unit> {
        val result = safeApiCall {
            apiService.getPrayerCalendarByGregorianMonth(
                year = year,
                month = month,
                latitude = latitude,
                longitude = longitude,
                method = calculationMethod,
                school = madhab,
            )
        }

        return when (result) {
            is NetworkResult.Success -> {
                val schedules = result.data.mapNotNull { prayerData ->
                    prayerData.toEntity(
                        latitude = latitude,
                        longitude = longitude,
                    )
                }

                // Prevents an empty or incomplete API response from deleting valid cache.
                if (schedules.isEmpty() || schedules.size != result.data.size) {
                    return NetworkResult.Error(
                        message = "Prayer schedule data is incomplete.",
                    )
                }

                try {
                    // Replaces the old monthly prayer schedule atomically.
                    prayerDao.replaceAll(schedules)
                    // Saves the coordinates used by the latest successful synchronization.
                    dataStoreManager.save(
                        key = PreferenceKeys.LATITUDE,
                        value = latitude,
                    )
                    dataStoreManager.save(
                        key = PreferenceKeys.LONGITUDE,
                        value = longitude,
                    )
                    NetworkResult.Success(
                        data = Unit,
                        message = result.message,
                    )
                } catch (error: CancellationException) {
                    throw error
                } catch (error: Exception) {
                    Timber.e(error, "Failed to save prayer schedules")
                    NetworkResult.Error(
                        message = "Failed to save prayer schedules.",
                    )
                }
            }

            is NetworkResult.Error -> result
            NetworkResult.Loading -> NetworkResult.Loading
        }
    }

    // Downloads one year of Islamic calendar data and replaces its Room cache.
    suspend fun syncIslamicCalendar(
        year: Int,
        latitude: Double,
        longitude: Double,
    ): NetworkResult<Unit> {
        val result = safeApiCall {
            apiService.getIslamicCalendarByGregorianYear(
                year = year,
                latitude = latitude,
                longitude = longitude,
            )
        }

        return when (result) {
            is NetworkResult.Success -> {
                // Converts the monthly response map into one yearly list.
                val calendarData = result.data.values.flatten()
                val calendarEntities = calendarData.mapNotNull { calendarDate ->
                    calendarDate.toIslamicCalendarEntity()
                }
                // Prevents invalid annual data from replacing valid calendar cache.
                if (calendarEntities.isEmpty() || calendarEntities.size != calendarData.size) {
                    return NetworkResult.Error(
                        message = "Islamic calendar data is incomplete.",
                    )
                }

                try {
                    // Replaces the old annual Islamic calendar atomically.
                    islamicCalendarDao.replaceAll(calendarEntities)
                    NetworkResult.Success(
                        data = Unit,
                        message = result.message,
                    )
                } catch (error: CancellationException) {
                    throw error
                } catch (error: Exception) {
                    Timber.e(error, "Failed to save Islamic calendar")
                    NetworkResult.Error(
                        message = "Failed to save Islamic calendar.",
                    )
                }
            }

            is NetworkResult.Error -> result
            NetworkResult.Loading -> NetworkResult.Loading
        }
    }

    // Returns today's cached prayer schedule for startup checking.
    suspend fun getPrayerScheduleByDate(
        date: String,
    ): PrayerEntity? {
        return prayerDao.getScheduleByDate(date)
    }

    // Observes prayer schedule changes for a particular date.
    fun observePrayerScheduleByDate(
        date: String,
    ): Flow<PrayerEntity?> {
        return prayerDao.getScheduleByDateFlow(date)
    }

    // Observes all cached prayer schedules for the current month.
    fun observePrayerSchedules(): Flow<List<PrayerEntity>> {
        return prayerDao.getAllSchedules()
    }

    // Checks whether Islamic calendar data for the requested year is cached.
    suspend fun hasIslamicCalendarForYear(
        year: Int,
    ): Boolean {
        return islamicCalendarDao.hasCalendarForYear(year)
    }

    // Observes Hijri date information and events for the selected date.
    fun observeIslamicCalendarByDate(
        date: String,
    ): Flow<IslamicCalendarEntity?> {
        return islamicCalendarDao.getCalendarByDateFlow(date)
    }

    // Observes all Islamic calendar entries in the requested year.
    fun observeIslamicCalendarByYear(
        year: Int,
    ): Flow<List<IslamicCalendarEntity>> {
        return islamicCalendarDao.getCalendarByYear(year)
    }
}
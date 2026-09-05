package com.project.prayerreminder.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.prayerreminder.core.data.local.entity.PrayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerDao {

    // save and replace all prayer schedule until 1 month
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(schedule: List<PrayerEntity>)

    // Get prayer schedule by date for this day
    @Query("Select * From prayer_schedules Where date = :date Limit 1")
    fun getScheduleByDate(date: String): PrayerEntity?

    // Get prayer schedule by a specific date for UI realtime
    @Query("Select * From prayer_schedules Where date = :date Limit 1")
    fun getScheduleByDateFlow(date: String): Flow<PrayerEntity?>

    // Get all 1-month prayer schedule
    @Query("Select * From prayer_schedules Order By date Asc")
    fun getAllSchedules(): Flow<List<PrayerEntity>>

    // Delete all prayer schedules (for user when refresh new location)
    @Query("Delete From prayer_schedules")
    suspend fun deleteAllSchedules()
}
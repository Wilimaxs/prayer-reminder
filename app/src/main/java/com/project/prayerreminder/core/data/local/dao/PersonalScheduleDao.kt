package com.project.prayerreminder.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.project.prayerreminder.core.data.local.entity.PersonalScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalScheduleDao {

    // Inserts a new schedule or updates an existing schedule with the same ID.
    @Upsert
    suspend fun upsertSchedule(schedule: PersonalScheduleEntity)

    // Observes all personal schedules for the selected calendar date.
    @Query("SELECT * FROM personal_schedules WHERE scheduleDate = :date ORDER BY scheduleTime ASC")
    fun getSchedulesByDate(date: String): Flow<List<PersonalScheduleEntity>>

    // Retrieves one schedule for an edit or detail operation.
    @Query("SELECT * FROM personal_schedules WHERE id = :scheduleId LIMIT 1")
    suspend fun getScheduleById(scheduleId: Long): PersonalScheduleEntity?

    // Retrieves every schedule when alarms need to be restored.
    @Query("SELECT * FROM personal_schedules")
    suspend fun getAllSchedules(): List<PersonalScheduleEntity>

    // Deletes a schedule directly using its ID.
    @Query("DELETE FROM personal_schedules WHERE id = :scheduleId")
    suspend fun deleteScheduleById(scheduleId: Long)
}

package com.project.prayerreminder.core.data.repository

import com.project.prayerreminder.core.data.local.dao.PersonalScheduleDao
import com.project.prayerreminder.core.data.local.entity.PersonalScheduleEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonalScheduleRepository @Inject constructor(
    private val personalScheduleDao: PersonalScheduleDao,
) {

    // Observes personal schedules for the selected calendar date.
    fun observeSchedulesByDate(
        date: String,
    ): Flow<List<PersonalScheduleEntity>> {
        return personalScheduleDao.getSchedulesByDate(date)
    }

    // Creates a new schedule or updates an existing schedule with the same ID.
    suspend fun upsertSchedule(schedule: PersonalScheduleEntity) {
        personalScheduleDao.upsertSchedule(schedule)
    }

    // Returns all personal schedules for alarm restoration.
    suspend fun getAllSchedules(): List<PersonalScheduleEntity> {
        return personalScheduleDao.getAllSchedules()
    }

    // Deletes a personal schedule using its Room ID.
    suspend fun deleteSchedule(scheduleId: Long) {
        personalScheduleDao.deleteScheduleById(scheduleId)
    }
}

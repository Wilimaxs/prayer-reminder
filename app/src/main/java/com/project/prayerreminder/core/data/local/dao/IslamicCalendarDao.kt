package com.project.prayerreminder.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.project.prayerreminder.core.data.local.entity.IslamicCalendarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IslamicCalendarDao {

    // Saves all calendar dates and replaces matching existing dates.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(calendarDates: List<IslamicCalendarEntity>)

    // Observes Hijri information and Islamic events for a selected date.
    @Query("SELECT * FROM islamic_calendar WHERE date = :date LIMIT 1")
    fun getCalendarByDateFlow(date: String): Flow<IslamicCalendarEntity?>

    // Returns one calendar entry for a selected Gregorian date.
    @Query("SELECT * FROM islamic_calendar WHERE date = :date LIMIT 1")
    suspend fun getCalendarByDate(date: String): IslamicCalendarEntity?

    // Observes all calendar dates within a Gregorian year.
    @Query("SELECT * FROM islamic_calendar WHERE gregorianYear = :year ORDER BY gregorianMonth ASC, gregorianDay ASC")
    fun getCalendarByYear(year: Int): Flow<List<IslamicCalendarEntity>>

    // Checks whether the current Gregorian year has already been cached.
    @Query(" SELECT EXISTS( SELECT 1 FROM islamic_calendar WHERE gregorianYear = :year)")
    suspend fun hasCalendarForYear(year: Int): Boolean

    // Removes the previously cached annual calendar.
    @Query("DELETE FROM islamic_calendar")
    suspend fun deleteAll()

    // Replaces annual calendar data within one atomic transaction.
    @Transaction
    suspend fun replaceAll(
        calendarDates: List<IslamicCalendarEntity>,
    ) {
        deleteAll()
        insertAll(calendarDates)
    }
}
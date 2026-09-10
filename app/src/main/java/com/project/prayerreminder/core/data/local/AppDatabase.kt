package com.project.prayerreminder.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.project.prayerreminder.core.data.local.converter.StringListConverter
import com.project.prayerreminder.core.data.local.dao.IslamicCalendarDao
import com.project.prayerreminder.core.data.local.dao.PersonalScheduleDao
import com.project.prayerreminder.core.data.local.dao.PrayerDao
import com.project.prayerreminder.core.data.local.entity.PersonalScheduleEntity
import com.project.prayerreminder.core.data.local.entity.IslamicCalendarEntity
import com.project.prayerreminder.core.data.local.entity.PrayerEntity

@Database(
    entities = [
        PrayerEntity::class,
        IslamicCalendarEntity::class,
        PersonalScheduleEntity::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun prayerDao(): PrayerDao
    abstract fun islamicCalendarDao(): IslamicCalendarDao
    abstract fun personalScheduleDao(): PersonalScheduleDao
}
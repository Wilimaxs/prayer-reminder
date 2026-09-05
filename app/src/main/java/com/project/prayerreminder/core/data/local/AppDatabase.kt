package com.project.prayerreminder.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.prayerreminder.core.data.local.dao.PrayerDao
import com.project.prayerreminder.core.data.local.entity.PrayerEntity

@Database(
    entities = [PrayerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun prayerDao(): PrayerDao
}
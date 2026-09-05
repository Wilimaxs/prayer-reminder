package com.project.prayerreminder.core.di

import android.content.Context
import androidx.room.Room
import com.project.prayerreminder.core.data.local.AppDatabase
import com.project.prayerreminder.core.data.local.dao.PrayerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "prayer_reminder_db"
        ).fallbackToDestructiveMigration(dropAllTables = true).build()
    }

    @Provides
    @Singleton
    fun providePrayerDao(
        appDatabase: AppDatabase
    ): PrayerDao {
        return appDatabase.prayerDao()
    }
}
package com.project.prayerreminder.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "personal_schedules",
    indices = [
        Index(value = ["scheduleDate"]),
    ],
)
data class PersonalScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    // Uses the same DD-MM-YYYY format as PrayerEntity.
    val scheduleDate: String,
    // Stores time using the HH:mm format.
    val scheduleTime: String,
    val isReminderEnabled: Boolean,
    // Stores how many minutes before the schedule the reminder should appear.
    val reminderOffsetMinutes: Int,
)
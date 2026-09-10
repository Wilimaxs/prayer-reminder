package com.project.prayerreminder.core.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "islamic_calendar",
    indices = [
        Index(value = ["gregorianYear"]),
    ],
)
data class IslamicCalendarEntity(
    @PrimaryKey
    val date: String, // Format: "DD-MM-YYYY"
    val gregorianDay: Int,
    val gregorianMonth: Int,
    val gregorianYear: Int,
    val hijriDay: Int,
    val hijriMonth: Int,
    val hijriYear: Int,
    // Contains Islamic events returned by Aladhan.
    val islamicEvents: List<String>,
)
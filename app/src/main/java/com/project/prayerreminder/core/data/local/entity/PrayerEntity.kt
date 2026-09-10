package com.project.prayerreminder.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_schedules")
data class PrayerEntity(
    @PrimaryKey
    val date: String, // Format: "DD-MM-YYYY"
    val fajr: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val hijriDay: Int,
    val hijriMonth: Int,
    val hijriYear: Int,
    val latitude: Double,
    val longitude: Double,
)
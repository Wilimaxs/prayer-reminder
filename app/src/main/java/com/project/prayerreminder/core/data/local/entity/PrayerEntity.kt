package com.project.prayerreminder.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_schedules")
data class PrayerEntity(
    @PrimaryKey
    val date: String, // Format: "DD-MM-YYYY" (Example: "05-09-2026")
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val imsak: String,
    val readableDate: String,
    val hijriDate: String,
    val hijriDay: String,
    val hijriMonthEn: String,
    val hijriMonthAr: String,
    val hijriYear: String,
    val dayNameEn: String,
    val latitude: Double,
    val longitude: Double
)
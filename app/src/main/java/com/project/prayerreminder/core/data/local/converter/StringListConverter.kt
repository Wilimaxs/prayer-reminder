package com.project.prayerreminder.core.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StringListConverter {

    private val gson = Gson()

    // Converts a string list into JSON before saving it to Room.
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    // Converts stored JSON back into a string list.
    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type

        return runCatching {
            gson.fromJson<List<String>>(value, listType)
        }.getOrNull().orEmpty()
    }
}
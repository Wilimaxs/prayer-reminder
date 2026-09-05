package com.project.prayerreminder.core.data.local.pref

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val AUTO_LOCATION = booleanPreferencesKey("auto_location")
    val MADZHAB = stringPreferencesKey("madzhab")
    val LATITUDE = doublePreferencesKey("latitude")
    val LONGITUDE = doublePreferencesKey("longitude")
    val CITY_NAME = stringPreferencesKey("city_name")

    // Key for complex data (JSON String)
    val PRAYER_ALARM_MAP = stringPreferencesKey("prayer_alarm_map")
}
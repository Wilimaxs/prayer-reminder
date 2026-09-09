package com.project.prayerreminder.core.data.local.pref

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val LATITUDE = doublePreferencesKey("latitude")
    val LONGITUDE = doublePreferencesKey("longitude")
    val CITY_NAME = stringPreferencesKey("city_name")
    val CALCULATION_METHOD = intPreferencesKey("calculation_method")
    val MADZHAB = intPreferencesKey("madhab_api_code")
    val PRAYER_REMINDERS_ENABLED = booleanPreferencesKey("prayer_reminders_enabled")
    val REMINDER_OFFSET_MINUTES = intPreferencesKey("reminder_offset_minutes")
    val ADZAN_SOUND = stringPreferencesKey("adzan_sound")
    val APP_LANGUAGE = stringPreferencesKey("app_language")
}
package com.project.prayerreminder.core.data.local.pref

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val LATITUDE = doublePreferencesKey("latitude")
    val LONGITUDE = doublePreferencesKey("longitude")
    val CALCULATION_METHOD = intPreferencesKey("calculation_method")
    val MADZHAB = intPreferencesKey("madhab_api_code")
    val PRAYER_REMINDERS_ENABLED = booleanPreferencesKey("prayer_reminders_enabled")
    val REMINDER_OFFSET_MINUTES = intPreferencesKey("reminder_offset_minutes")
    val APP_LANGUAGE = stringPreferencesKey("app_language")
    val PRAYER_SETTING_CHANGE_COUNT = intPreferencesKey("prayer_setting_change_count")
    val PRAYER_SETTING_CHANGE_WINDOW_STARTED_AT =
        longPreferencesKey("prayer_setting_change_window_started_at")
    val LAST_SEEN_ANNOUNCEMENT_ID = stringPreferencesKey("last_seen_announcement_id")
}

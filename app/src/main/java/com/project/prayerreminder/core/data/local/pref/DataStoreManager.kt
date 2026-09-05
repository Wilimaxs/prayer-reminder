package com.project.prayerreminder.core.data.local.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    @PublishedApi internal val dataStore: DataStore<Preferences>,
    @PublishedApi internal val gson: Gson
) {

    // Save data primitive (include: Boolean, String, Int, Double, Long)
    suspend fun <T> save(key: Preferences.Key<T>, value: T) {
        dataStore.edit { pref ->
            pref[key] = value
        }
    }

    // Read data primitive with flow
    fun <T> get(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return dataStore.data.map { pref ->
            pref[key] ?: defaultValue
        }
    }

    // save data object/map/list complex from JSON String
    suspend fun <T> saveObject(key: Preferences.Key<String>, value: T) {
        val jsonString = gson.toJson(value)
        save(key, jsonString)
    }

    // Read Object / Map / List complex from JSON String
    inline fun <reified T> getObject(key: Preferences.Key<String>): Flow<T?> {
        return dataStore.data.map { pref ->
            val jsonString = pref[key]
            if (!jsonString.isNullOrEmpty()) {
                try {
                    gson.fromJson(jsonString, T::class.java)
                } catch (e: Exception) {
                    Timber.e(e, "Failed to parse JSON from DataStore for key: ${key.name}")
                    null
                }
            } else null
        }
    }

    // Delete a specific key
    suspend fun <T> remove(key: Preferences.Key<T>) {
        dataStore.edit { pref ->
            pref.remove(key)
        }
    }
}
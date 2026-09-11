package com.project.prayerreminder.core.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.project.prayerreminder.BuildConfig
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class RemoteConfigManager @Inject constructor() {

    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    var currentConfig: RemoteAppConfig = RemoteAppConfig()
        private set

    // Fetches Remote Config and falls back to activated or in-app default values.
    suspend fun fetchAndActivate(): RemoteAppConfig {
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setFetchTimeoutInSeconds(FETCH_TIMEOUT_SECONDS)
            .setMinimumFetchIntervalInSeconds(
                if (BuildConfig.DEBUG) 0L else FETCH_INTERVAL_SECONDS,
            )
            .build()

        try {
            remoteConfig.setConfigSettingsAsync(settings).awaitCompletion()
            remoteConfig.setDefaultsAsync(
                mapOf<String, Any>(
                    MAINTENANCE_ENABLED to false,
                    MINIMUM_SUPPORTED_VERSION to BuildConfig.VERSION_CODE.toLong(),
                    LATEST_VERSION to BuildConfig.VERSION_CODE.toLong(),
                    ANNOUNCEMENT_ID to "",
                    ANNOUNCEMENT_MESSAGE to "",
                )
            ).awaitCompletion()
            remoteConfig.fetchAndActivate().awaitCompletion()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Timber.e(exception, "Unable to fetch Remote Config; using cached values")
        }

        currentConfig = RemoteAppConfig(
            maintenanceEnabled = remoteConfig.getBoolean(MAINTENANCE_ENABLED),
            minimumSupportedVersion = remoteConfig.getLong(MINIMUM_SUPPORTED_VERSION),
            latestVersion = remoteConfig.getLong(LATEST_VERSION),
            announcementId = remoteConfig.getString(ANNOUNCEMENT_ID).trim(),
            announcementMessage = remoteConfig.getString(ANNOUNCEMENT_MESSAGE).trim(),
        )

        return currentConfig
    }

    private suspend fun Task<*>.awaitCompletion() {
        suspendCancellableCoroutine { continuation ->
            addOnCompleteListener { task ->
                if (!continuation.isActive) return@addOnCompleteListener

                if (task.isSuccessful) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(
                        task.exception ?: IllegalStateException("Firebase task failed"),
                    )
                }
            }
        }
    }

    companion object {
        private const val MAINTENANCE_ENABLED = "maintenance_enabled"
        private const val MINIMUM_SUPPORTED_VERSION = "minimum_supported_version"
        private const val LATEST_VERSION = "latest_version"
        private const val ANNOUNCEMENT_ID = "announcement_id"
        private const val ANNOUNCEMENT_MESSAGE = "announcement_message"
        private const val FETCH_TIMEOUT_SECONDS = 10L
        private const val FETCH_INTERVAL_SECONDS = 3_600L
    }
}

data class RemoteAppConfig(
    val maintenanceEnabled: Boolean = false,
    val minimumSupportedVersion: Long = 0L,
    val latestVersion: Long = 0L,
    val announcementId: String = "",
    val announcementMessage: String = "",
)

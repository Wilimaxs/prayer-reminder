package com.project.prayerreminder.core.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

data class LocationAddress(
    val city: String?,
    val province: String?,
    val country: String?,
) {
    // Builds the short location label displayed on Home.
    fun toDisplayName(): String? {
        val cityOrProvince = city ?: province

        return listOfNotNull(
            cityOrProvince,
            country,
        ).distinct()
            .joinToString(", ")
            .takeIf { it.isNotBlank() }
    }
}

@Singleton
class LocationAddressResolver @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {

    // Converts coordinates into an address without blocking the main thread.
    suspend fun resolve(
        latitude: Double,
        longitude: Double,
        locale: Locale,
    ): LocationAddress? {
        if (!Geocoder.isPresent()) return null

        return try {
            val geocoder = Geocoder(context, locale)
            val address = withTimeoutOrNull(GEOCODER_TIMEOUT) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    resolveAsynchronously(
                        geocoder = geocoder,
                        latitude = latitude,
                        longitude = longitude,
                    )
                } else {
                    resolveOnBackgroundThread(
                        geocoder = geocoder,
                        latitude = latitude,
                        longitude = longitude,
                    )
                }
            } ?: return null

            LocationAddress(
                city = address.locality
                    ?: address.subAdminArea,
                province = address.adminArea,
                country = address.countryName,
            )
        } catch (error: Exception) {
            Timber.w(error, "Failed to resolve location address")
            null
        }
    }

    // Uses the non-blocking Geocoder API available from Android 13.
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun resolveAsynchronously(
        geocoder: Geocoder,
        latitude: Double,
        longitude: Double,
    ): Address? {
        return suspendCancellableCoroutine { continuation ->
            geocoder.getFromLocation(
                latitude,
                longitude,
                MAX_RESULTS,
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        if (continuation.isActive) {
                            continuation.resume(addresses.firstOrNull())
                        }
                    }

                    override fun onError(errorMessage: String?) {
                        if (continuation.isActive) {
                            continuation.resume(null)
                        }
                    }
                },
            )
        }
    }

    // Keeps the deprecated blocking API away from the main thread on older Android versions.
    @Suppress("DEPRECATION")
    private suspend fun resolveOnBackgroundThread(
        geocoder: Geocoder,
        latitude: Double,
        longitude: Double,
    ): Address? {
        return withContext(Dispatchers.IO) {
            geocoder.getFromLocation(
                latitude,
                longitude,
                MAX_RESULTS,
            )?.firstOrNull()
        }
    }

    companion object {
        private const val MAX_RESULTS = 1
        private const val GEOCODER_TIMEOUT = 10_000L
    }
}

package com.project.prayerreminder.feature.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.project.prayerreminder.R
import com.project.prayerreminder.core.theme.PrayerDimens
import com.project.prayerreminder.utils.composables.AppButton
import com.project.prayerreminder.utils.composables.AppButtonVariant
import com.project.prayerreminder.utils.composables.AppSnackbarType
import com.project.prayerreminder.utils.composables.LocalAppSnackbarHostState
import com.project.prayerreminder.utils.composables.showAppSnackbar
import androidx.core.net.toUri
import timber.log.Timber

@SuppressLint("MissingPermission")
@Composable
fun SplashScreen(
    modifier: Modifier,
    onFinished: (SplashResult) -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val snackbarHostState = LocalAppSnackbarHostState.current
    val fusedLocationClient = remember(context) {
        LocationServices.getFusedLocationProviderClient(context)
    }
    val locationManager = remember(context) {
        context.getSystemService(android.content.Context.LOCATION_SERVICE)
                as android.location.LocationManager
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val synchronizationErrorTitle = stringResource(R.string.splash_sync_failed_title)
    val synchronizationErrorSubtitle = stringResource(R.string.splash_tap_to_retry)

    // Opens the application page in Google Play with a browser fallback.
    val openApplicationStore: () -> Unit = {
        val marketIntent = Intent(
            Intent.ACTION_VIEW,
            "market://details?id=${context.packageName}".toUri(),
        )

        runCatching {
            context.startActivity(marketIntent)
        }.onFailure {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=${context.packageName}".toUri(),
                )
            )
        }
    }

    // Checks whether either precise or approximate location permission is available.
    fun hasLocationPermission(): Boolean {
        val hasFineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

        return hasFineLocation || hasCoarseLocation
    }

    // Requests a fresh device location after permission and location services are available.
    val requestDeviceLocation = {
        if (!hasLocationPermission()) {
            viewModel.onLocationUnavailable(LocationSettingsTarget.Application)
        } else if (!LocationManagerCompat.isLocationEnabled(locationManager)) {
            viewModel.onLocationUnavailable(LocationSettingsTarget.Location)
        } else {
            val cancellationTokenSource = CancellationTokenSource()

            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token,
            ).addOnSuccessListener { location ->
                if (location != null) {
                    Timber.d(
                        "Location acquired: lat=${location.latitude}, lng=${location.longitude}, accuracy=${location.accuracy}"
                    )
                    viewModel.onLocationAvailable(
                        latitude = location.latitude,
                        longitude = location.longitude,
                    )
                } else {
                    Timber.w("getCurrentLocation returned null")
                    viewModel.onLocationUnavailable(LocationSettingsTarget.Location)
                }
            }.addOnFailureListener {
                Timber.e("getCurrentLocation failed")
                viewModel.onLocationUnavailable(LocationSettingsTarget.Location)
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        val permissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (permissionGranted) {
            requestDeviceLocation()
        } else {
            viewModel.onLocationUnavailable(LocationSettingsTarget.Application)
        }
    }

    // Requests permission on the first launch or reads location when it is already granted.
    fun requestLocationAccess() {
        if (hasLocationPermission()) {
            requestDeviceLocation()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            )
        }
    }

    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) {
        // Rechecks the required location after returning from Android Settings.
        viewModel.onLocationSettingsReturned()
    }

    // Requests location only according to the cache condition determined by ViewModel.
    LaunchedEffect(uiState.locationCheckMode) {
        when (uiState.locationCheckMode) {
            SplashLocationCheckMode.Required -> {
                requestLocationAccess()
            }

            SplashLocationCheckMode.Optional -> {
                when {
                    !hasLocationPermission() -> {
                        viewModel.onLocationUnavailable(
                            LocationSettingsTarget.Application,
                        )
                    }

                    !LocationManagerCompat.isLocationEnabled(locationManager) -> {
                        viewModel.onLocationUnavailable(
                            LocationSettingsTarget.Location,
                        )
                    }

                    else -> {
                        requestDeviceLocation()
                    }
                }
            }

            null -> Unit
        }
    }

    LaunchedEffect(uiState.isFinished, uiState.result) {
        val result = uiState.result

        if (uiState.isFinished && result != null) {
            onFinished(result)
        }
    }

    // Displays initialization errors and allows retry by tapping the Snackbar.
    LaunchedEffect(uiState.errorMessage) {
        val errorMessage = uiState.errorMessage ?: return@LaunchedEffect
        viewModel.onErrorShown()

        snackbarHostState.showAppSnackbar(
            title = synchronizationErrorTitle,
            subtitle = "$errorMessage $synchronizationErrorSubtitle",
            type = AppSnackbarType.ERROR,
            duration = SnackbarDuration.Long,
            onClick = viewModel::retryInitialization,
        )
    }

    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_splash_logo),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.height(PrayerDimens.StackSmall))
            Text(
                text = stringResource(R.string.splash_title),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
            Spacer(modifier = Modifier.height(PrayerDimens.Baseline))
            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(PrayerDimens.StackMedium))
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(0.5f),
                progress = { uiState.currentProgress },
            )
            Spacer(modifier = Modifier.height(PrayerDimens.StackSmall))
            Text(
                text = stringResource(uiState.progressMessage.textRes),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

    if (uiState.showLocationDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(text = stringResource(R.string.splash_location_unavailable_title))
            },
            text = {
                Text(text = stringResource(R.string.splash_location_unavailable_description))
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(PrayerDimens.StackSmall),
                ) {
                    AppButton(
                        text = stringResource(R.string.continue_with_jakarta),
                        onClick = viewModel::continueWithDefaultLocation,
                        modifier = Modifier.weight(1f),
                        variant = AppButtonVariant.Outlined,
                    )
                    AppButton(
                        text = stringResource(R.string.open_settings),
                        onClick = {
                            val intent = when (uiState.locationSettingsTarget) {
                                LocationSettingsTarget.Location -> {
                                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                }

                                LocationSettingsTarget.Application -> {
                                    Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        "package:${context.packageName}".toUri(),
                                    )
                                }
                            }

                            viewModel.onLocationSettingsOpened()
                            settingsLauncher.launch(intent)
                        },
                        modifier = Modifier.weight(1f),
                    )
                }
            },
        )
    }

    when (uiState.remoteDialog) {
        SplashRemoteDialog.Maintenance -> {
            AlertDialog(
                onDismissRequest = {},
                title = {
                    Text(text = stringResource(R.string.maintenance_title))
                },
                text = {
                    Text(text = stringResource(R.string.maintenance_description))
                },
                confirmButton = {},
            )
        }

        SplashRemoteDialog.ForceUpdate -> {
            AlertDialog(
                onDismissRequest = {},
                title = {
                    Text(text = stringResource(R.string.update_required_title))
                },
                text = {
                    Text(text = stringResource(R.string.update_required_description))
                },
                confirmButton = {
                    AppButton(
                        text = stringResource(R.string.update_now),
                        onClick = openApplicationStore,
                    )
                },
            )
        }

        SplashRemoteDialog.SoftUpdate -> {
            AlertDialog(
                onDismissRequest = viewModel::continueAfterOptionalUpdate,
                title = {
                    Text(text = stringResource(R.string.update_available_title))
                },
                text = {
                    Text(text = stringResource(R.string.update_available_description))
                },
                dismissButton = {
                    AppButton(
                        text = stringResource(R.string.later),
                        onClick = viewModel::continueAfterOptionalUpdate,
                        variant = AppButtonVariant.Outlined,
                    )
                },
                confirmButton = {
                    AppButton(
                        text = stringResource(R.string.update_now),
                        onClick = openApplicationStore,
                    )
                },
            )
        }

        null -> Unit
    }
}

private val SplashProgressMessage.textRes: Int
    get() = when (this) {
        SplashProgressMessage.CheckingApplication -> R.string.splash_checking_application
        SplashProgressMessage.CheckingLocation -> R.string.splash_checking_location
        SplashProgressMessage.CheckingPrayerSchedule -> R.string.splash_checking_prayer_schedule
        SplashProgressMessage.SynchronizingPrayerSchedule -> R.string.splash_syncing_prayer_schedule
        SplashProgressMessage.CheckingIslamicCalendar -> R.string.splash_checking_islamic_calendar
        SplashProgressMessage.SynchronizingIslamicCalendar -> R.string.splash_syncing_islamic_calendar
        SplashProgressMessage.Finishing -> R.string.splash_finishing
    }

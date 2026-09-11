package com.project.prayerreminder.feature.splash

data class SplashUiState(
    val currentProgress: Float = 0f,
    val progressMessage: SplashProgressMessage = SplashProgressMessage.CheckingApplication,
    val isInitializing: Boolean = false,
    val showLocationDialog: Boolean = false,
    val locationSettingsTarget: LocationSettingsTarget = LocationSettingsTarget.Location,
    val isFinished: Boolean = false,
    val result: SplashResult? = null,
    val errorMessage: String? = null,
    val locationCheckMode: SplashLocationCheckMode? = null,
    val remoteDialog: SplashRemoteDialog? = null,
)

enum class SplashProgressMessage {
    CheckingApplication,
    CheckingLocation,
    CheckingPrayerSchedule,
    SynchronizingPrayerSchedule,
    CheckingIslamicCalendar,
    SynchronizingIslamicCalendar,
    Finishing,
}

enum class LocationSettingsTarget {
    Location,
    Application,
}

enum class SplashResult {
    CacheReady,
    SyncSuccess,
    CalendarSyncFailed,
}

enum class SplashLocationCheckMode {
    Optional,
    Required,
}

enum class SplashRemoteDialog {
    Maintenance,
    ForceUpdate,
    SoftUpdate,
}

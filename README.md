# Prayer Reminder

Prayer Reminder is a native Android application built with Kotlin and Jetpack Compose.  
The project is designed as a portfolio/showcase application that focuses on offline-first data handling, local persistence, prayer schedule synchronization, Islamic calendar data, localization, device location, reminders, and Firebase-based application monitoring.

The application retrieves prayer schedules and Islamic calendar data from the Aladhan API, stores the required data locally with Room, stores user preferences with DataStore, and continues to provide useful data even when the device is offline after synchronization.

## Portfolio

Project showcase and downloadable release:

https://basehub.me/

## Main Features

- Daily prayer schedule based on the user's current location
- Prayer times for:
    - Fajr
    - Dhuhr
    - Asr
    - Maghrib
    - Isha
- Next-prayer information
- Gregorian and Hijri calendar information
- Islamic event / holiday information
- Personal schedule management
- Local prayer reminders and personal schedule reminders
- Prayer calculation method settings
- Asr madhab selection
- Reminder offset settings
- English and Indonesian localization
- Location-based prayer schedule synchronization
- Cached prayer data for offline usage
- Cached Islamic calendar data for offline usage
- Firebase Remote Config for application-level remote controls
- Firebase Crashlytics for crash reporting
- Firebase Performance Monitoring
- Firebase Analytics
- Native Android splash screen
- Release build optimization with R8

## Offline-First Approach

Prayer Reminder uses an offline-first approach for the application's primary data.

The general flow is:

```text
Aladhan API
    |
    v
Remote DTO
    |
    v
Mapper
    |
    v
Room Database
    |
    v
ViewModel / StateFlow
    |
    v
Jetpack Compose UI
```

After successful synchronization, the application reads prayer schedules and Islamic calendar information from Room instead of depending on the API for every screen load.

User preferences are stored locally with DataStore.

## Data Synchronization

### Prayer Schedule

Prayer schedule data is synchronized per Gregorian month using the Aladhan API.

```text
GET /v1/calendar/{year}/{month}
```

The request uses:

- latitude
- longitude
- calculation method
- madhab / school

The synchronized prayer schedule is cached in Room together with the coordinates used for synchronization.

The application can compare the latest device location with the cached location and synchronize prayer data again when the user has moved far enough from the previous location.

### Islamic Calendar

Islamic calendar data is synchronized for the current Gregorian year.

```text
GET /v1/calendar/{year}
```

The application stores the required Hijri date and Islamic holiday information locally in Room.

## Location

The application uses Google Play Services Fused Location Provider.

Current location is requested using high-accuracy priority when synchronization requires device coordinates.

The application supports:

- precise location
- approximate location
- location-service availability checks
- fallback handling when location is unavailable

If required location data cannot be obtained, the application can continue using Jakarta, Indonesia as the default location.

Reverse geocoding is used to convert coordinates into a readable location label such as city and country.

## Local Storage

### Room

Room is used for structured application data, including:

- prayer schedules
- Islamic calendar data
- personal schedules

Prayer schedules are cached monthly, while Islamic calendar data is cached yearly.

### DataStore

Preferences DataStore is used for application settings such as:

- application language
- calculation method
- Asr madhab
- prayer reminder state
- reminder offset
- cached configuration values

## Reminder System

The application uses Android `AlarmManager` for prayer reminders and personal schedule reminders.

When exact alarms are permitted, the application uses:

```text
setExactAndAllowWhileIdle()
```

When exact alarm access is unavailable, the application falls back to:

```text
setAndAllowWhileIdle()
```

Scheduled alarms are restored after:

- device reboot
- application package update

A `BootReceiver` handles alarm rescheduling using Room-backed data.

> Note: This project is primarily intended as a portfolio/showcase application. Local reminder behavior may still be affected by Android power-management policies and vendor-specific battery restrictions.

## Localization

The application currently supports:

- English
- Bahasa Indonesia

Android string resources are separated by locale:

```text
res/values/strings.xml
res/values-in/strings.xml
```

The selected language is stored in DataStore and applied at application level so all screens can use standard Android string resources.

## Firebase

Firebase is used for monitoring and remote application configuration.

### Remote Config

Firebase Remote Config supports application-level controls such as:

- maintenance mode
- minimum supported version
- latest available version
- announcements

The application keeps safe local defaults if Remote Config cannot be fetched.

### Crashlytics

Firebase Crashlytics is enabled for release crash reporting.

### Performance Monitoring

Firebase Performance Monitoring is used to observe application performance and network behavior.

### Analytics

Firebase Analytics is included for application event tracking.

## Tech Stack

### Android

- Kotlin
- Jetpack Compose
- Material 3
- AndroidX Navigation Compose
- AndroidX SplashScreen

### Architecture and State

- ViewModel
- StateFlow
- Coroutines
- Hilt dependency injection

### Local Data

- Room
- Preferences DataStore

### Networking

- Retrofit
- OkHttp
- Gson
- Aladhan API

### Location

- Google Play Services Location
- Android Geocoder

### Background / Reminder

- AlarmManager
- BroadcastReceiver
- BootReceiver
- Android notifications

### Firebase

- Firebase Remote Config
- Firebase Crashlytics
- Firebase Performance Monitoring
- Firebase Analytics

### UI Utilities

- Kizitonwose Calendar Compose
- Timber

## Project Configuration

Current Android configuration:

```text
Minimum SDK : 26
Target SDK  : 37
Java        : 17
Kotlin      : 2.4.10
```

The project uses Gradle Kotlin DSL and a Version Catalog.

## Project Structure

A simplified view of the project structure:

```text
app/src/main/java/com/project/prayerreminder/
|
|-- core/
|   |-- alarm/
|   |-- data/
|   |   |-- local/
|   |   |   |-- dao/
|   |   |   |-- entity/
|   |   |   |-- mapper/
|   |   |   `-- pref/
|   |   |-- remote/
|   |   |   `-- model/
|   |   `-- repository/
|   |-- firebase/
|   |-- location/
|   |-- theme/
|   `-- di/
|
|-- feature/
|   |-- splash/
|   |-- home/
|   |-- calendar/
|   `-- profile/
|
|-- receiver/
|-- route/
|-- utils/
|
|-- MainActivity.kt
`-- PrayerApplication.kt
```

The project is organized primarily by feature while reusable application infrastructure is placed inside `core`.

## Build Requirements

Before running the project, make sure you have:

- Android Studio
- JDK 17
- Android SDK compatible with the project's compile / target SDK
- an Android device or emulator
- a Firebase project configuration if Firebase functionality is required

## Firebase Configuration

The project expects:

```text
app/google-services.json
```

The Firebase configuration file is intentionally excluded from Git.

You need to register your own Android application in Firebase and place your own `google-services.json` file inside the `app` module.

## Running the Project

Clone the repository:

```bash
git clone https://github.com/Wilimaxs/prayer-reminder.git
cd prayer-reminder
```

Then open the project with Android Studio and allow Gradle synchronization to finish.

Run the application on an Android device or emulator.

For location testing on an emulator, configure a simulated device location from Android Emulator Extended Controls.

## Release Build

For a signed release build, use Android Studio:

```text
Build
-> Generate Signed App Bundle or APK
-> APK
-> Select / create keystore
-> release
```

The release configuration enables R8 minification and optimization.

## Permissions

The application may use the following Android permissions:

```text
INTERNET
ACCESS_NETWORK_STATE
ACCESS_FINE_LOCATION
ACCESS_COARSE_LOCATION
POST_NOTIFICATIONS
SCHEDULE_EXACT_ALARM
RECEIVE_BOOT_COMPLETED
```

Some permissions are only required on specific Android versions.

## Privacy Notes

The application does not require a user account.

Primary application data is stored locally on the device.

Location coordinates are used to request prayer schedules from the Aladhan API during synchronization.

Firebase may process technical diagnostic, analytics, crash, and performance information according to the Firebase services enabled in the application.

## Development Scope

Prayer Reminder was built primarily as an Android development showcase.

The project intentionally prioritizes demonstrating:

- Jetpack Compose
- Kotlin application logic
- offline-first architecture
- Room persistence
- DataStore preferences
- Retrofit API integration
- location services
- Android background scheduling
- application localization
- Firebase integration
- feature-oriented project organization

Some production-hardening concerns such as long-term database migration strategy, advanced exact-alarm permission UX, and final backup policy are intentionally kept lightweight because this repository is currently intended as a showcase project rather than a continuously maintained production application.

## Tested Environment

The main application flow has been tested on:

- physical Android 12 device
- Android 14 emulator

This includes location retrieval, application startup, local data synchronization, and the primary application flow.

## API

Prayer schedule and Islamic calendar data are provided by:

Aladhan API  
https://aladhan.com/prayer-times-api

Please refer to the Aladhan documentation for API usage terms and current endpoint behavior.

## Developer

Developed by **Wildan**  
Mobile Developer

Portfolio:

https://basehub.me/

GitHub:

https://github.com/Wilimaxs

## Repository Purpose

This repository is published primarily for portfolio, learning, and technical showcase purposes.

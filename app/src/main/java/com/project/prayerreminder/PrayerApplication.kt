package com.project.prayerreminder

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.project.prayerreminder.core.data.local.pref.DataStoreManager
import com.project.prayerreminder.core.data.local.pref.PreferenceKeys
import com.project.prayerreminder.receiver.PrayerAlarmReceiver
import com.project.prayerreminder.utils.enumeration.AppLanguage
import dagger.hilt.EntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@HiltAndroidApp
class PrayerApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var currentActivity: Activity? = null
    var currentLanguage: AppLanguage = AppLanguage.English
        private set

    private val activityCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(
            activity: Activity,
            savedInstanceState: Bundle?,
        ) = Unit

        override fun onActivityStarted(activity: Activity) {
            currentActivity = activity
        }

        override fun onActivityResumed(activity: Activity) {
            currentActivity = activity
        }

        override fun onActivityPaused(activity: Activity) = Unit

        override fun onActivityStopped(activity: Activity) {
            if (currentActivity === activity) {
                currentActivity = null
            }
        }

        override fun onActivitySaveInstanceState(
            activity: Activity,
            outState: Bundle,
        ) = Unit

        override fun onActivityDestroyed(activity: Activity) {
            if (currentActivity === activity) {
                currentActivity = null
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(activityCallbacks)

        applyStoredLanguage()
        PrayerAlarmReceiver.createNotificationChannel(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun onTerminate() {
        unregisterActivityLifecycleCallbacks(activityCallbacks)
        applicationScope.cancel()
        super.onTerminate()
    }

    private fun applyStoredLanguage() {
        val dataStoreManager = EntryPointAccessors.fromApplication(
            this,
            PrayerApplicationEntryPoint::class.java,
        ).dataStoreManager()

        val initialLanguageTag = runBlocking(Dispatchers.IO) {
            dataStoreManager.get(
                key = PreferenceKeys.APP_LANGUAGE,
                defaultValue = AppLanguage.English.languageTag,
            ).first()
        }
        currentLanguage = AppLanguage.fromLanguageTag(initialLanguageTag)

        applicationScope.launch {
            dataStoreManager.get(
                key = PreferenceKeys.APP_LANGUAGE,
                defaultValue = AppLanguage.English.languageTag,
            )
                .map(AppLanguage::fromLanguageTag)
                .distinctUntilChanged()
                .collect { language ->
                    val previousLanguage = currentLanguage
                    currentLanguage = language
                    if (previousLanguage != language) {
                        currentActivity?.recreate()
                    }
                }
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PrayerApplicationEntryPoint {
    fun dataStoreManager(): DataStoreManager
}

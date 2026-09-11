package com.project.prayerreminder.core.firebase

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashlyticsTree : Timber.Tree() {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    // Sends warning and error logs to Crashlytics without reporting routine logs.
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        if (priority < Log.WARN) return

        crashlytics.log("${tag.orEmpty()}: $message")

        if (priority >= Log.ERROR && t != null) {
            crashlytics.recordException(t)
        }
    }
}

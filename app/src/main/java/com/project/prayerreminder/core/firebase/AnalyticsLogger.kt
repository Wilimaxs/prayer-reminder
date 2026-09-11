package com.project.prayerreminder.core.firebase

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsLogger @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val analytics = FirebaseAnalytics.getInstance(context)

    // Sends only explicitly provided, non-sensitive event parameters to Firebase Analytics.
    fun log(
        eventName: String,
        vararg parameters: Pair<String, Any>,
    ) {
        val bundle = Bundle().apply {
            parameters.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Boolean -> putString(key, value.toString())
                    is Int -> putLong(key, value.toLong())
                    is Long -> putLong(key, value)
                    is Double -> putDouble(key, value)
                    is Float -> putDouble(key, value.toDouble())
                }
            }
        }

        analytics.logEvent(eventName, bundle)
    }

    companion object {
        const val EVENT_PRAYER_SYNC = "prayer_sync"
        const val EVENT_CALENDAR_SYNC = "calendar_sync"
        const val EVENT_PRAYER_SETTING_SYNC = "prayer_setting_sync"
        const val EVENT_PRAYER_REMINDER_CHANGED = "prayer_reminder_changed"
        const val EVENT_REMINDER_OFFSET_CHANGED = "reminder_offset_changed"
        const val EVENT_PERSONAL_SCHEDULE_CHANGED = "personal_schedule_changed"
        const val EVENT_REMOTE_GATE_SHOWN = "remote_gate_shown"
        const val EVENT_ANNOUNCEMENT_VIEWED = "announcement_viewed"

        const val PARAM_RESULT = "result"
        const val PARAM_TYPE = "type"
        const val PARAM_ENABLED = "enabled"
        const val PARAM_OFFSET_MINUTES = "offset_minutes"
        const val PARAM_ACTION = "action"
        const val PARAM_ANNOUNCEMENT_ID = "announcement_id"

        const val RESULT_SUCCESS = "success"
        const val RESULT_FAILED = "failed"
        const val RESULT_RATE_LIMITED = "rate_limited"
    }
}

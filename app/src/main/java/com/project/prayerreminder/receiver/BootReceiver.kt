package com.project.prayerreminder.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.project.prayerreminder.core.alarm.AlarmScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != Intent.ACTION_MY_PACKAGE_REPLACED
        ) return

        val pendingResult = goAsync()

        // Restores Room-backed alarms after reboot or an application update.
        CoroutineScope(Dispatchers.IO).launch {
            try {
                alarmScheduler.rescheduleAllAlarms()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Timber.e(exception, "Failed to restore alarms")
            } finally {
                pendingResult.finish()
            }
        }
    }
}

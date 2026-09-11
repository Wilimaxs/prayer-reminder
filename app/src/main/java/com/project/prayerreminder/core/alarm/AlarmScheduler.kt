package com.project.prayerreminder.core.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.core.net.toUri
import com.project.prayerreminder.R
import com.project.prayerreminder.core.data.local.entity.PersonalScheduleEntity
import com.project.prayerreminder.core.data.local.entity.PrayerEntity
import com.project.prayerreminder.core.data.local.pref.DataStoreManager
import com.project.prayerreminder.core.data.local.pref.PreferenceKeys
import com.project.prayerreminder.core.data.repository.PersonalScheduleRepository
import com.project.prayerreminder.core.data.repository.PrayerRepository
import com.project.prayerreminder.receiver.PrayerAlarmReceiver
import com.project.prayerreminder.utils.enumeration.AppLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val prayerRepository: PrayerRepository,
    private val personalScheduleRepository: PersonalScheduleRepository,
    private val dataStoreManager: DataStoreManager,
) {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    // Restores every future alarm, including after a device reboot.
    suspend fun rescheduleAllAlarms() {
        reschedulePrayerAlarms()
        reschedulePersonalAlarms()
    }

    // Replaces prayer alarms using Room schedules and the current DataStore preferences.
    suspend fun reschedulePrayerAlarms() {
        val schedules = prayerRepository.observePrayerSchedules().first()
        val isEnabled = dataStoreManager.get(
            key = PreferenceKeys.PRAYER_REMINDERS_ENABLED,
            defaultValue = true,
        ).first()
        val reminderOffsetMinutes = dataStoreManager.get(
            key = PreferenceKeys.REMINDER_OFFSET_MINUTES,
            defaultValue = 10,
        ).first()
        val localizedContext = getLocalizedContext()

        schedules.forEach { schedule ->
            schedule.prayerTimes().forEach prayerLoop@{ prayer ->
                val pendingIntent = createPrayerPendingIntent(
                    schedule = schedule,
                    prayer = prayer,
                    reminderOffsetMinutes = reminderOffsetMinutes,
                    localizedContext = localizedContext,
                )

                alarmManager.cancel(pendingIntent)

                if (isEnabled) {
                    val triggerAtMillis = prayer.triggerAtMillis(
                        date = schedule.date,
                        reminderOffsetMinutes = reminderOffsetMinutes,
                    ) ?: return@prayerLoop

                    scheduleAlarm(
                        triggerAtMillis = triggerAtMillis,
                        pendingIntent = pendingIntent,
                    )
                }
            }
        }
    }

    // Replaces future personal schedule alarms stored in Room.
    suspend fun reschedulePersonalAlarms() {
        val localizedContext = getLocalizedContext()

        personalScheduleRepository.getAllSchedules().forEach scheduleLoop@{ schedule ->
            val pendingIntent = createPersonalSchedulePendingIntent(
                schedule = schedule,
                localizedContext = localizedContext,
            )

            alarmManager.cancel(pendingIntent)

            if (schedule.isReminderEnabled) {
                val triggerAtMillis = schedule.triggerAtMillis() ?: return@scheduleLoop

                scheduleAlarm(
                    triggerAtMillis = triggerAtMillis,
                    pendingIntent = pendingIntent,
                )
            }
        }
    }

    // Removes the alarm belonging to a deleted personal schedule.
    fun cancelPersonalSchedule(scheduleId: Long) {
        alarmManager.cancel(
            createAlarmPendingIntent(
                identifier = "personal/$scheduleId",
                title = "",
                description = "",
            )
        )
    }

    // Uses an exact alarm when Android permits it and a safe fallback otherwise.
    private fun scheduleAlarm(
        triggerAtMillis: Long,
        pendingIntent: PendingIntent,
    ) {
        if (triggerAtMillis <= System.currentTimeMillis()) return

        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
                alarmManager.canScheduleExactAlarms()
            ) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent,
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent,
                )
            }
        } catch (exception: SecurityException) {
            Timber.e(exception, "Exact alarm permission is unavailable")
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent,
            )
        }
    }

    private fun createPrayerPendingIntent(
        schedule: PrayerEntity,
        prayer: PrayerTime,
        reminderOffsetMinutes: Int,
        localizedContext: Context,
    ): PendingIntent {
        val prayerName = localizedContext.getString(prayer.nameRes)
        val description = if (reminderOffsetMinutes == 0) {
            localizedContext.getString(
                R.string.notification_prayer_at_time,
                prayerName,
            )
        } else {
            localizedContext.getString(
                R.string.notification_prayer_before,
                prayerName,
                reminderOffsetMinutes,
                prayer.time,
            )
        }

        return createAlarmPendingIntent(
            identifier = "prayer/${schedule.date}/${prayer.key}",
            title = localizedContext.getString(R.string.notification_prayer_title),
            description = description,
        )
    }

    private fun createPersonalSchedulePendingIntent(
        schedule: PersonalScheduleEntity,
        localizedContext: Context,
    ): PendingIntent {
        val description = if (schedule.reminderOffsetMinutes == 0) {
            localizedContext.getString(R.string.notification_schedule_at_time)
        } else {
            localizedContext.getString(
                R.string.notification_schedule_before,
                schedule.reminderOffsetMinutes,
                schedule.scheduleTime,
            )
        }

        return createAlarmPendingIntent(
            identifier = "personal/${schedule.id}",
            title = schedule.title,
            description = description,
        )
    }

    private fun createAlarmPendingIntent(
        identifier: String,
        title: String,
        description: String,
    ): PendingIntent {
        val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            action = PrayerAlarmReceiver.ACTION_SHOW_ALARM
            data = "prayerreminder://alarm/$identifier".toUri()
            putExtra(PrayerAlarmReceiver.EXTRA_TITLE, title)
            putExtra(PrayerAlarmReceiver.EXTRA_DESCRIPTION, description)
        }

        return PendingIntent.getBroadcast(
            context,
            identifier.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private suspend fun getLocalizedContext(): Context {
        val languageTag = dataStoreManager.get(
            key = PreferenceKeys.APP_LANGUAGE,
            defaultValue = AppLanguage.English.languageTag,
        ).first()
        val configuration = Configuration(context.resources.configuration).apply {
            setLocale(Locale.forLanguageTag(languageTag))
        }

        return context.createConfigurationContext(configuration)
    }

    private fun PrayerEntity.prayerTimes(): List<PrayerTime> {
        return listOf(
            PrayerTime("fajr", R.string.fajr, fajr),
            PrayerTime("dhuhr", R.string.dhuhr, dhuhr),
            PrayerTime("asr", R.string.asr, asr),
            PrayerTime("maghrib", R.string.maghrib, maghrib),
            PrayerTime("isha", R.string.isha, isha),
        )
    }

    private fun PrayerTime.triggerAtMillis(
        date: String,
        reminderOffsetMinutes: Int,
    ): Long? {
        return runCatching {
            LocalDateTime.of(
                LocalDate.parse(date, DATE_FORMATTER),
                LocalTime.parse(time, TIME_FORMATTER),
            )
                .minusMinutes(reminderOffsetMinutes.toLong())
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }.onFailure { error ->
            Timber.e(error, "Unable to parse prayer alarm time")
        }.getOrNull()
    }

    private fun PersonalScheduleEntity.triggerAtMillis(): Long? {
        return runCatching {
            LocalDateTime.of(
                LocalDate.parse(scheduleDate, DATE_FORMATTER),
                LocalTime.parse(scheduleTime, TIME_FORMATTER),
            )
                .minusMinutes(reminderOffsetMinutes.toLong())
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }.onFailure { error ->
            Timber.e(error, "Unable to parse personal schedule alarm time")
        }.getOrNull()
    }

    private data class PrayerTime(
        val key: String,
        val nameRes: Int,
        val time: String,
    )

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        private val TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
    }
}

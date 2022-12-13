package com.chihwhsu.atto.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.chihwhsu.atto.component.AlarmReceiver
import java.util.*

object AlarmManagerUtil {

    fun setAlarmTime(context: Context, time: Long, intent: Intent) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val sender = PendingIntent
            .getBroadcast(
                context,
                intent.getIntExtra(ID, 0),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val interval = intent.getLongExtra(INTERVAL_MILLIS, 0)
        alarmManager.setWindow(AlarmManager.RTC_WAKEUP, time, interval, sender)
    }

    fun cancelAlarm(context: Context, id: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(action)
        val intent = Intent(context, AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(sender)
    }

    fun setAlarm(
        context: Context,
        flag: Int,
        hour: Int,
        minute: Int,
        id: Int,
        week: Int,
        ringTone: String?,
        soundOrVibrator: Int,
        duration: Long? = null
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        var intervalMillis: Long = 0
        calendar[calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH], hour, minute] =
            10

        when (flag) {
            0 -> {
                intervalMillis = 0
            }
            1 -> {
                intervalMillis = DAY
            }
            2 -> {
                intervalMillis = WEEK
            }
        }
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(INTERVAL_MILLIS, intervalMillis)
        intent.putExtra(RINGTONE, ringTone)
        intent.putExtra(ID, id)
        intent.putExtra(SOUND_OR_VIBRATOR, soundOrVibrator)
        intent.putExtra(DURATION, duration)
        val sender =
            PendingIntent.getBroadcast(
                context,
                id,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        alarmManager.setWindow(
            AlarmManager.RTC_WAKEUP, calMethod(week, calendar.timeInMillis),
            intervalMillis, sender
        )
    }

    private fun calMethod(weekFlag: Int, dateTime: Long): Long {
        var time: Long = 0

        if (weekFlag != 0) {
            val c = Calendar.getInstance()
            var week = c[Calendar.DAY_OF_WEEK]
            when (week) {
                1 -> {
                    week = 7
                }
                2 -> {
                    week = 1
                }
                3 -> {
                    week = 2
                }
                4 -> {
                    week = 3
                }
                5 -> {
                    week = 4
                }
                6 -> {
                    week = 5
                }
                7 -> {
                    week = 6
                }
            }
            if (weekFlag == week) {
                time = if (dateTime > System.currentTimeMillis()) {
                    dateTime
                } else {
                    dateTime + WEEK
                }
            } else if (weekFlag > week) {
                time = dateTime + (weekFlag - week) * DAY
            } else if (weekFlag < week) {
                time = dateTime + (weekFlag - week + 7) * DAY
            }
        } else {
            time = if (dateTime > System.currentTimeMillis()) {
                dateTime
            } else {
                dateTime + DAY
            }
        }

        time -= 10000L

        return time
    }


    private const val RINGTONE = "ringTone"
    private const val ID = "id"
    private const val SOUND_OR_VIBRATOR = "soundOrVibrator"
    private const val INTERVAL_MILLIS = "intervalMillis"
    private const val DURATION = "duration"

}

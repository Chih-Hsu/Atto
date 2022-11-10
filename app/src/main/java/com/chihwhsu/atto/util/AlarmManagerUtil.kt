package com.chihwhsu.atto.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.chihwhsu.atto.component.AlarmReceiver
import java.util.*

object AlarmManagerUtil {

    val ALARM_ACTION = "com.atto.alarm"

    fun setAlarmTime(context: Context, time: Long, intent: Intent) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val sender = PendingIntent
            .getBroadcast(
                context,
                intent.getIntExtra("id", 0), intent, PendingIntent.FLAG_CANCEL_CURRENT
            )

        val interval = intent.getLongExtra("intervalMills",0)

        alarmManager.setWindow(AlarmManager.RTC_WAKEUP,time,interval,sender)
    }

    fun cancelAlarm(context: Context,action:String,id:Int){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(action)
        val sender = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
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
        tips: String?,
        soundOrVibrator: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        var intervalMillis: Long = 0
        calendar[calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH], hour, minute] =
            10
        if (flag == 0) {
            intervalMillis = 0
        } else if (flag == 1) {
            intervalMillis = (24 * 3600 * 1000).toLong()
        } else if (flag == 2) {
            intervalMillis = (24 * 3600 * 1000 * 7).toLong()
        }
        val intent = Intent(context, AlarmReceiver::class.java)
//        val intent = Intent(ALARM_ACTION)
        intent.putExtra("intervalMillis", intervalMillis)
        intent.putExtra("msg", tips)
        intent.putExtra("id", id)
        intent.putExtra("soundOrVibrator", soundOrVibrator)
        val sender =
            PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            alarmManager.setWindow(
                AlarmManager.RTC_WAKEUP, AlarmManagerUtil.calMethod(week, calendar.timeInMillis),
                intervalMillis, sender
            )

    }

    private fun calMethod(weekFlag: Int, dateTime: Long): Long {
        var time: Long = 0

        if (weekFlag != 0) {
            val c = Calendar.getInstance()
            var week = c[Calendar.DAY_OF_WEEK]
            if (1 == week) {
                week = 7
            } else if (2 == week) {
                week = 1
            } else if (3 == week) {
                week = 2
            } else if (4 == week) {
                week = 3
            } else if (5 == week) {
                week = 4
            } else if (6 == week) {
                week = 5
            } else if (7 == week) {
                week = 6
            }
            if (weekFlag == week) {
                time = if (dateTime > System.currentTimeMillis()) {
                    dateTime
                } else {
                    dateTime + 7 * 24 * 3600 * 1000
                }
            } else if (weekFlag > week) {
                time = dateTime + (weekFlag - week) * 24 * 3600 * 1000
            } else if (weekFlag < week) {
                time = dateTime + (weekFlag - week + 7) * 24 * 3600 * 1000
            }
        } else {
            time = if (dateTime > System.currentTimeMillis()) {
                dateTime
            } else {
                dateTime + 24 * 3600 * 1000
            }
        }
        return time
    }



}
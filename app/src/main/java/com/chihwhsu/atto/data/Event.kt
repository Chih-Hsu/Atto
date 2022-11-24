package com.chihwhsu.atto.data

import android.content.Context
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.chihwhsu.atto.data.database.EventConverter
import com.chihwhsu.atto.ext.getTimeFromStartOfDay
import com.chihwhsu.atto.util.AlarmManagerUtil
import java.util.*

@Entity(tableName = "event_table")
@TypeConverters(EventConverter::class)
data class Event(
    @PrimaryKey(autoGenerate = false)
    val id: Int = UUID.randomUUID().hashCode(),
    @ColumnInfo(name = "alarm_time")
    val alarmTime: Long = 0L,
    @ColumnInfo(name = "alarm_uri")
    val alarmSoundUri: Uri = Uri.EMPTY,
    @ColumnInfo(name = "alarm_name")
    val alarmSoundName: String = "",
    @ColumnInfo(name = "type")
    val type: Int = -1,
    @ColumnInfo(name = "routine")
    val routine: List<Boolean>? = emptyList(), //only alarm
    @ColumnInfo(name = "vibration")
    val vibration: Boolean? = false, //only alarm
    @ColumnInfo(name = "snooze_mode")
    val snoozeMode: Boolean? = false, //only alarm
    @ColumnInfo(name = "title")
    val title: String? = "", //only todolist
    @ColumnInfo(name = "content")
    val content: String? = "", // only todolist
    @ColumnInfo(name = "start_time")
    val startTime: Long? = 0L, // only pomodoro
    @ColumnInfo(name = "lock_app")
    val lockApp: Boolean? = false, // only pomodoro
    @ColumnInfo(name = "lock_app_label")
    val lockAppLabel: String? = "",// label only pomodoro

) {

    companion object {
        const val ALARM_TYPE = 1
        const val TODO_TYPE = 2
        const val POMODORO_WORK_TYPE = 3
        const val POMODORO_BREAK_TYPE = 4

    }

    fun setAlarmTime(applicationContext: Context, newId: Int) {

        val remindWay = if (vibration == true) {
            2
        } else if (vibration == false) {
            1
        } else {
            0
        }

        val time = getTimeFromStartOfDay(alarmTime)
        val hours = time / (1000 * 60 * 60)
        val minutes = time / (1000 * 60) - hours * 60

        // 2 is repeat every week, 0 is one time , 1 is everyday
        val flag = if (snoozeMode == true) 2 else 0

        val routineDays = mutableListOf<Int>()
        routine?.let {
            for (boolean in it) {
                if (boolean) {
                    routineDays.add(it.indexOf(boolean) + 1)  // index of monday is 0 , so + 1
                }
            }
        }

        if (routineDays.isEmpty()) {
            AlarmManagerUtil.setAlarm(
                // applicationContext only
                applicationContext,
                flag,
                hours.toInt(),
                minutes.toInt(),
                newId,
                0,
                alarmSoundUri.toString(),
                remindWay,

            )
        } else {
            for (day in routineDays) {
                AlarmManagerUtil.setAlarm(
                    // applicationContext only
                    applicationContext,
                    flag,
                    hours.toInt(),
                    minutes.toInt(),
                    newId,
                    day,
                    alarmSoundUri.toString(),
                    remindWay,
                )
            }
        }
    }

    fun stopAlarm(applicationContext: Context) {
        AlarmManagerUtil.cancelAlarm(
            applicationContext, id
        )
    }


    fun setPomodoroAlarmTime(applicationContext: Context, newId: Int, duration: Long) {

        val time = getTimeFromStartOfDay(startTime!!)
        val hours = time / (1000 * 60 * 60)
        val minutes = time / (1000 * 60) - hours * 60


        AlarmManagerUtil.setAlarm(
            // applicationContext only
            applicationContext,
            0,
            hours.toInt(),
            minutes.toInt(),
            newId,
            0,
            "content://media/internal/audio/media/203?title=Crayon%20Rock&canonical=1",
            1,
            duration
        )

    }

}
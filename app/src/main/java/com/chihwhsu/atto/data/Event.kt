package com.chihwhsu.atto.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.chihwhsu.atto.data.database.EventConverter
import java.lang.reflect.Array.set

@Entity(tableName = "event_table")
@TypeConverters(EventConverter::class)
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "alarm_time")
    val alarmTime: Long = 0L,
    @ColumnInfo(name = "alarm_uri")
    val alarmSoundUri: Uri = Uri.EMPTY,
    @ColumnInfo(name = "alarm_name")
    val alarmSoundName: String ="",
    @ColumnInfo(name = "type")
    val type: Int = -1,
    @ColumnInfo(name = "routine")
    val routine: List<Boolean>? = emptyList(), //only alarm
    @ColumnInfo(name = "vibration")
    val vibration : Boolean? = false, //only alarm
    @ColumnInfo(name = "snooze_mode")
    val snoozeMode : Boolean? = false, //only alarm
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

    companion object{
        const val ALARM_TYPE = 1
        const val TODO_TYPE = 2
        const val POMODORO_WORK_TYPE = 3
        const val POMODORO_BREAK_TYPE = 4
    }




}
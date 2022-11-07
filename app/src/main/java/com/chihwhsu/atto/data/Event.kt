package com.chihwhsu.atto.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.chihwhsu.atto.data.database.EventConverter

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
    @ColumnInfo(name = "alarm_day")
    val alarmDay : Long,
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


    // type = 1  Alarm
    // type = 2  TodoList
    // type = 3  Pomodoro
}
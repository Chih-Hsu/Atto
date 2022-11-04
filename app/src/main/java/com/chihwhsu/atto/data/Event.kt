package com.chihwhsu.atto.data

import android.net.Uri
import androidx.room.Entity

@Entity(tableName = "event_table")
data class Event(
    val id: Long = 0L,
    val alarmTime: Long = 0L,
    val alarmSoundUri: Uri = Uri.EMPTY,
    val alarmSoundName: String ="",
    val type: Int = -1,
    val routine: List<Boolean>? = emptyList(), //only alarm
    val vibration : Boolean? = false, //only alarm
    val snoozeMode : Boolean? = false, //only alarm
    val title: String? = "", //only todolist
    val content: String? = "", // only todolist
    val startTime: Long? = 0L, // only pomodoro
    val lockApp: Boolean? = false, // only pomodoro
    val lockAppList: List<String>? = emptyList(),// packageName only pomodoro

) {


    // type = 1  Alarm
    // type = 2  TodoList
    // type = 3  Pomodoro
}
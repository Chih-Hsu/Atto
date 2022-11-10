package com.chihwhsu.atto.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


// When user set a usage limit for a app
//  record the targetTime and everyTime user start the app
// launch a foreground service to count the usage time

@Entity(tableName = "usage_tracker_table",
    indices = [Index(value = ["package_name"], unique = true)])

data class AppLockTimer(
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0,
    @ColumnInfo(name = "package_name")
    val packageName:String,
    @ColumnInfo(name = "start_time")
    val startTime:Long = 0,
    @ColumnInfo(name = "target_time")
    val targetTime:Long = 0,
) {
}
package com.chihwhsu.atto.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "timezone_table",
    indices = [Index(value = ["locale"], unique = true)])
data class AttoTimeZone(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    @ColumnInfo(name = "locale")
    val locale :String = "",
    @ColumnInfo(name = "name")
    val name : String = "",
    @ColumnInfo(name = "sort")
    val sort : Int = -1
) {
}
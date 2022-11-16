package com.chihwhsu.atto.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "widget_table",
    indices = [Index(value = ["label"], unique = true)])
data class Widget(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L,
    @ColumnInfo(name = "label")
    val label: String,
) {
}
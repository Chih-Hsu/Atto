package com.chihwhsu.atto.data

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "widget_table")
data class Widget(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L,
    @ColumnInfo(name = "label")
    val label: String,
) {
}
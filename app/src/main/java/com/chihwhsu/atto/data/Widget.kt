package com.chihwhsu.atto.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "widget_table",
    indices = [Index(value = ["label"], unique = true)]
)
data class Widget(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "label")
    val label: String,
) : Parcelable

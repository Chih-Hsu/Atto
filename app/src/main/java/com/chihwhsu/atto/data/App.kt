package com.chihwhsu.atto.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.chihwhsu.atto.data.database.AttoConverter
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.util.UsageStatesManager
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "app_table")
@TypeConverters(AttoConverter::class)
data class App (
    @PrimaryKey(autoGenerate = false)
    val appLabel : String,
    @ColumnInfo(name = "package_name")
    val packageName : String,
    @ColumnInfo(name = "icon")
    val icon : Bitmap?,
    @ColumnInfo(name = "label")
    val label : String? = null,
    @ColumnInfo(name = "is_enable")
    val isEnable : Boolean = true,
    @ColumnInfo(name = "theme")
    val theme : Int = -1,
    @ColumnInfo(name = "installed")
    val installed : Boolean = true,
    @ColumnInfo(name = "sort")
    val sort : Int = -1
        ) :Parcelable {


    fun getUsage(context: Context) =
        UsageStatesManager.getUsage(context,packageName)

    fun lock(databaseDao: AttoDatabaseDao,hours:Int,minutes:Int){

    }
}
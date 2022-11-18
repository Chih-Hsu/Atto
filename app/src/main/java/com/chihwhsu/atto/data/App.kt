package com.chihwhsu.atto.data

import android.content.Context
import android.graphics.Bitmap
import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.chihwhsu.atto.data.database.AttoConverter
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.util.UsageStatesManager
import kotlinx.parcelize.Parcelize
import java.io.File


@Parcelize
@Entity(tableName = "app_table")
//@TypeConverters(AttoConverter::class)
data class App (
    @PrimaryKey(autoGenerate = false)
    val appLabel : String ="",
    @ColumnInfo(name = "package_name")
    val packageName : String ="",
    @ColumnInfo(name = "icon_path")
    val iconPath : String ="",
    @ColumnInfo(name = "label")
    val label : String? = null,
    @ColumnInfo(name = "is_enable")
    val isEnable : Boolean = true,
    @ColumnInfo(name = "theme")
    val theme : Int = -1,
    @ColumnInfo(name = "installed")
    val installed : Boolean = true,
    @ColumnInfo(name = "sort")
    val sort : Int = -1,
        ) :Parcelable {


    fun getTodayUsage(context: Context) =
        UsageStatesManager.getTodayUsage(context,packageName)

    fun getTotalUsage(context: Context) =
        UsageStatesManager.getTotalUsage(context,packageName)

    fun get24HourUsageList(context: Context) =
        UsageStatesManager.get24hrUsageList(context,packageName)


    fun getUsageTimeFromStart(context: Context, startTime : Long):Long{
        return UsageStatesManager.getUsageFromStartTime(context,packageName,startTime)
    }

    fun analyseStorage(context: Context) : Long {
        val appBaseFolder = context.filesDir.parentFile
        val totalSize = browseFiles(appBaseFolder)
        return totalSize
    }

    private fun browseFiles(dir: File): Long {
        var dirSize: Long = 0
        for (f in dir.listFiles()) {
            dirSize += f.length()
            if (f.isDirectory) {
                dirSize += browseFiles(f)
            }
        }
        return dirSize
    }
}
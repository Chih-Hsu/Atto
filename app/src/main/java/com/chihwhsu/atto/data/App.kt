package com.chihwhsu.atto.data

import android.content.Context
import android.os.Parcelable
import android.os.StatFs
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chihwhsu.atto.util.UsageStatesManager
import kotlinx.parcelize.Parcelize
import java.io.File
import java.text.NumberFormat


@Parcelize
@Entity(tableName = "app_table")
//@TypeConverters(AttoConverter::class)
data class App (
    @PrimaryKey(autoGenerate = false)
    val appLabel : String ="",
    @ColumnInfo(name = "package_name")
    val packageName : String ="",
    @ColumnInfo(name = "icon_path")
    var iconPath : String ="",
    @ColumnInfo(name = "label")
    val label : String? = null,
    @ColumnInfo(name = "is_enable")
    val isEnable : Boolean = true,
    @ColumnInfo(name = "theme")
    val theme : Int = -1,
    @ColumnInfo(name = "installed")
    var installed : Boolean = true,
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

    fun analyseStorage(context: Context) {
        val internalStorageFile: File = context.getFilesDir()
        val availableSizeInBytes = StatFs(internalStorageFile.getPath()).availableBytes
        val number = NumberFormat.getInstance().format(availableSizeInBytes);
        Log.d("storage","$number")
    }


}
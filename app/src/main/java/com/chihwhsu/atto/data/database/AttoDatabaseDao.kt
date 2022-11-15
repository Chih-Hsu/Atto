package com.chihwhsu.atto.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.AppLockTimer
import com.chihwhsu.atto.data.Event

@Dao
interface AttoDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(app: App)

    @Update
    fun update(app: App)

    @Query("Update app_table set label = :label where appLabel = :appName")
    fun updateLabel(appName: String, label: String?)

    @Query("UPDATE app_table set sort = :sort where appLabel = :appName")
    fun updateSort(appName: String, sort: Int)

    @Query("Update app_table set theme = :theme where appLabel = :appName")
    fun updateTheme(appName: String, theme: Int?)

    @Query("UPDATE app_table set is_enable = :doLock where package_name = :packageName")
    fun lockApp(packageName : String, doLock : Boolean)

    @Query("UPDATE app_table set is_enable = :notEnable where is_enable = :isEnable")
    fun lockAllApp(notEnable: Boolean,isEnable: Boolean)

    @Query("UPDATE app_table set is_enable =:notEnable where label = :label")
    fun lockSpecificLabelApp(label: String,notEnable: Boolean)

    @Query("UPDATE app_table set is_enable = :isEnable WHERE is_enable = :notEnable")
    fun unLockAllApp(isEnable: Boolean,notEnable:Boolean)

    @Query("UPDATE app_table set is_enable =:isEnable where label = :label")
    fun unLockSpecificLabelApp(label: String,isEnable: Boolean)

    @Query("DELETE from app_table WHERE package_name = :packageName")
    fun delete(packageName: String)

    @Query("DELETE FROM app_table")
    fun clear()

    @Query("SELECT * FROM app_table WHERE package_name = :packageName")
    fun getApp(packageName: String):App?

    @Query("SELECT * FROM app_table ORDER BY sort ASC")
    fun getAllApps(): LiveData<List<App>>

    @Query("Select * from app_table where label is NULL ")
    fun getNoLabelApps(): LiveData<List<App>>

    @Query("SELECT * from app_table where label = :label ")
    fun getSpecificLabelApps(label: String): LiveData<List<App>>

    @Query("select * from app_table where label not like '%dock%'")
    fun getAllAppsWithoutDock(): LiveData<List<App>>

    @Query("SELECT label FROM app_table")
    fun getLabelList(): LiveData<List<String>>

    @Query("SELECT * FROM app_table")
    fun getAllAppNotLiveData():List<App>?

    @Query("UPDATE app_table SET label = null WHERE label = :label")
    fun deleteSpecificLabel(label: String)


    // Event

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(event: Event)

//    @Query("SELECT * FROM event_table where alarm_time BETWEEN :todayStart and :todayEnd order by alarm_time")
//    fun getTodayEvents(todayStart:Long,todayEnd:Long) : LiveData<List<Event>>

    @Query("SELECT * FROM event_table order by alarm_time asc")
    fun getAllEvents(): LiveData<List<Event>>

    @Query("DELETE from event_table WHERE id = :id")
    fun deleteEvent(id: Int)

    @Query("SELECT * FROM event_table WHERE id = :id")
    fun getEvent(id: Int): Event

    @Query("SELECT * FROM event_table WHERE type = :type order by alarm_time asc")
    fun getTypeEvent(type: Int): LiveData<List<Event>>

    @Query("Update event_table set alarm_time = :newAlarmTime where id = :id")
    fun delayEvent5Minutes(id: Int, newAlarmTime: Long)

    @Query("SELECT EXISTS(SELECT * FROM event_table WHERE type = :type or :type2)")
    fun isPomodoroIsExist(type: Int,type2:Int) : Boolean



    // AppLockTimer

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(appLockTimer: AppLockTimer)

    @Query("DELETE FROM usage_tracker_table WHERE id = :id")
    fun deleteTimer(id : Long)

    @Query("DELETE FROM usage_tracker_table")
    fun deleteAllTimer()

    @Query("UPDATE usage_tracker_table SET target_time = :remainTime")
    fun updateTimer(remainTime : Long)

    @Query("SELECT * FROM usage_tracker_table WHERE package_name = :packageName")
    fun getTimer(packageName: String): AppLockTimer?

    @Query("SELECT * FROM usage_tracker_table ")
    fun getAllTimer(): LiveData<List<AppLockTimer>>

    // Widget

//    @Query("SELECT * FROM widget_table ORDER BY id")
//    fun getAllWidget()


}


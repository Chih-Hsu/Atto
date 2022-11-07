package com.chihwhsu.atto.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.Event

@Dao
interface AttoDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(app: App)

    @Update
    fun update(app: App)

    @Query("Update app_table set label = :label where appLabel = :appName")
    fun updateLabel(appName: String , label : String?)

    @Query("UPDATE app_table set sort = :sort where appLabel = :appName")
    fun updateSort(appName: String , sort:Int)

    @Query("Update app_table set theme = :theme where appLabel = :appName")
    fun updateTheme(appName: String , theme : Int?)

    @Query("DELETE from app_table WHERE package_name = :packageName")
    fun delete(packageName : String)

    @Query("DELETE FROM app_table")
    fun clear()

    @Query("SELECT * FROM app_table ORDER BY sort ASC")
    fun getAllApps(): LiveData<List<App>>

    @Query("Select * from app_table where label is NULL ")
    fun getNoLabelApps() : LiveData<List<App>>

    @Query("SELECT * from app_table where label = :label ")
    fun getSpecificLabelApps(label:String) : LiveData<List<App>>

    @Query("select * from app_table where label not like '%dock%'")
    fun getAllAppsWithoutDock():LiveData<List<App>>

    @Query("SELECT label FROM app_table")
    fun getLabelList():LiveData<List<String>>



    // Event

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(event: Event)

//    @Query("SELECT * FROM event_table where alarm_time BETWEEN :todayStart and :todayEnd order by alarm_time")
//    fun getTodayEvents(todayStart:Long,todayEnd:Long) : LiveData<List<Event>>

    @Query("SELECT * FROM event_table order by alarm_time asc")
    fun getAllEvents() : LiveData<List<Event>>

}


package com.chihwhsu.atto.data.database

import androidx.lifecycle.LiveData
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.AppLockTimer
import com.chihwhsu.atto.data.Event

interface AttoDataSource {


    // App

    suspend fun insert(app: App)


    suspend fun update(app: App)


    fun updateLabel(appName: String, label: String?)


    fun updateSort(appName: String, sort: Int)


    suspend fun updateTheme(appName: String, theme: Int?)


    suspend fun lockApp(packageName : String)


    suspend fun unLockAllApp()


    suspend fun delete(packageName: String)


    suspend fun clear()


    fun getApp(packageName: String): App?


    fun getAllApps(): LiveData<List<App>>


    fun getNoLabelApps(): LiveData<List<App>>


    fun getSpecificLabelApps(label: String): LiveData<List<App>>


    fun getAllAppsWithoutDock(): LiveData<List<App>>


    fun getLabelList(): LiveData<List<String>>

    suspend fun updateAppData()




    // Event


    suspend fun insert(event: Event)


    fun getAllEvents(): LiveData<List<Event>>


    suspend fun deleteEvent(id: Long)


    suspend fun getEvent(id: Long): Event


    fun getTypeEvent(type: Int): LiveData<List<Event>>


    suspend fun delayEvent5Minutes(id: Long)


    // AppLockTimer


    suspend fun insert(appLockTimer: AppLockTimer)


    suspend fun deleteTimer(id : Long)


    suspend fun deleteAllTimer()


    suspend fun updateTimer(remainTime : Long)


    suspend fun getTimer(packageName: String): AppLockTimer?


    fun getAllTimer(): LiveData<List<AppLockTimer>>

}
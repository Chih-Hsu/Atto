package com.chihwhsu.atto.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.chihwhsu.atto.data.*

interface AttoRepository {

    // App

    suspend fun insert(app: App)

    suspend fun update(app: App)

    fun updateLabel(appName: String, label: String?)

    fun updateSort(appName: String, sort: Int)

    fun updateIconPath(appName: String, path: String)

    fun updateAppInstalled(appName: String)

    suspend fun updateTheme(appName: String, theme: Int?)

    suspend fun lockApp(packageName: String)

    suspend fun unLockAllApp()

    fun unLockSpecificLabelApp(label: String)

    suspend fun delete(packageName: String)

    suspend fun clear()

    fun getApp(packageName: String): App?

    fun getAllApps(): LiveData<List<App>>

    fun getNoLabelApps(): LiveData<List<App>>

    fun getSpecificLabelApps(label: String): LiveData<List<App>>

    fun getAllAppsWithoutDock(): LiveData<List<App>>

    fun getLabelList(): LiveData<List<String>>

    suspend fun updateAppData()

    fun getAllAppNotLiveData(): List<App>?

    fun deleteSpecificLabel(label: String)


    // Event


    suspend fun insert(event: Event)

    fun getAllEvents(): LiveData<List<Event>>

    suspend fun deleteEvent(id: Int)

    suspend fun getEvent(id: Int): Event?

    fun getTypeEvent(type: Int): LiveData<List<Event>>

    suspend fun delayEvent5Minutes(id: Int)

    fun lockAllApp()

    fun lockSpecificLabelApp(label: String)

    fun isPomodoroIsExist(): Boolean


    // AppLockTimer


    suspend fun insert(appLockTimer: AppLockTimer)

    suspend fun deleteTimer(id: Long)

    suspend fun deleteAllTimer()

    suspend fun updateTimer(remainTime: Long)

    suspend fun getTimer(packageName: String): AppLockTimer?

    fun getAllTimer(): LiveData<List<AppLockTimer>>


    // Widget

    fun getAllWidget(): LiveData<List<Widget>>

    fun insert(widget: Widget)

    fun deleteWidget(id: Long)


    // Remote

    suspend fun getUser(email: String): Result<User>

    suspend fun syncRemoteData(
        context: Context,
        user: User,
        appList: List<App>
    ): Result<List<App>>

    suspend fun uploadData(context: Context, localAppList: List<App>):Result<Boolean>

    suspend fun uploadUser(user: User): Result<Boolean>


    // TimeZone

    fun insert(timeZone : AttoTimeZone)

    fun getAllTimeZone():LiveData<List<AttoTimeZone>>

    fun deleteTimeZone(id : Long)

}
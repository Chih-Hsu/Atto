package com.chihwhsu.atto.data.database


import android.content.Context
import androidx.lifecycle.LiveData
import com.chihwhsu.atto.data.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DefaultAttoRepository(
    private val attoRemoteDataSource: AttoDataSource,
    private val attoLocalDataSource: AttoDataSource,
    private val attoSystemDataSource: AttoDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AttoRepository {

    override suspend fun insert(app: App) {
        withContext(ioDispatcher) {
            attoLocalDataSource.insert(app)
        }
    }

    override suspend fun insert(event: Event) {
        attoLocalDataSource.insert(event)
    }

    override suspend fun insert(appLockTimer: AppLockTimer) {
        attoLocalDataSource.insert(appLockTimer)
    }

    override fun insert(widget: Widget) {
        attoLocalDataSource.insert(widget)
    }

    override fun insert(timeZone: AttoTimeZone) {
        attoLocalDataSource.insert(timeZone)
    }

    override suspend fun update(app: App) {
        withContext(ioDispatcher) {
            attoLocalDataSource.update(app)
        }
    }

    override fun updateLabel(appName: String, label: String?) {
        attoLocalDataSource.updateLabel(appName, label)
    }

    override fun updateSort(appName: String, sort: Int) {
        attoLocalDataSource.updateSort(appName, sort)
    }

    override fun updateIconPath(appName: String, path: String) {
        attoLocalDataSource.updateIconPath(appName, path)
    }

    override fun updateAppInstalled(appName: String) {
        attoLocalDataSource.updateAppInstalled(appName)
    }

    override suspend fun updateTheme(appName: String, theme: Int?) {
        attoLocalDataSource.updateTheme(appName, theme)
    }

    override suspend fun lockApp(packageName: String) {
        attoLocalDataSource.lockApp(packageName)
    }

    override suspend fun unLockAllApp() {
        attoLocalDataSource.unLockAllApp()
    }

    override fun unLockSpecificLabelApp(label: String) {
        attoLocalDataSource.unLockSpecificLabelApp(label)
    }

    override suspend fun delete(packageName: String) {
        withContext(ioDispatcher) {
            attoLocalDataSource.delete(packageName)
        }
    }

    override suspend fun clear() {
        attoLocalDataSource.clear()
    }

    override fun getApp(packageName: String): App? {
        return attoLocalDataSource.getApp(packageName)
    }

    override fun getAllApps(): LiveData<List<App>> {
        return attoLocalDataSource.getAllApps()
    }


    override fun getNoLabelApps(): LiveData<List<App>> {
        return attoLocalDataSource.getNoLabelApps()
    }

    override fun getSpecificLabelApps(label: String): LiveData<List<App>> {
        return attoLocalDataSource.getSpecificLabelApps(label)
    }

    override fun getAllAppsWithoutDock(): LiveData<List<App>> {
        return attoLocalDataSource.getAllAppsWithoutDock()
    }

    override fun getLabelList(): LiveData<List<String>> {
        return attoLocalDataSource.getLabelList()
    }

    override fun getAllEvents(): LiveData<List<Event>> {
        return attoLocalDataSource.getAllEvents()
    }

    override suspend fun deleteEvent(id: Int) {
        return attoLocalDataSource.deleteEvent(id)
    }

    override suspend fun getEvent(id: Int): Event? {
        return attoLocalDataSource.getEvent(id)
    }

    override fun getTypeEvent(type: Int): LiveData<List<Event>> {
        return attoLocalDataSource.getTypeEvent(type)
    }

    override suspend fun delayEvent5Minutes(id: Int) {
        return attoLocalDataSource.delayEvent5Minutes(id)
    }

    override fun lockAllApp() {
        attoLocalDataSource.lockAllApp()
    }

    override fun lockSpecificLabelApp(label: String) {
        attoLocalDataSource.lockSpecificLabelApp(label)
    }

    override fun isPomodoroIsExist(): Boolean {
        return attoLocalDataSource.isPomodoroIsExist()
    }

    override suspend fun deleteTimer(id: Long) {
        return attoLocalDataSource.deleteTimer(id)
    }

    override suspend fun deleteAllTimer() {
        attoLocalDataSource.deleteAllTimer()
    }

    override suspend fun updateTimer(remainTime: Long) {
        attoLocalDataSource.updateTimer(remainTime)
    }

    override suspend fun getTimer(packageName: String): AppLockTimer? {
        return attoLocalDataSource.getTimer(packageName)
    }

    override fun getAllTimer(): LiveData<List<AppLockTimer>> {
        return attoLocalDataSource.getAllTimer()
    }

    override fun getAllWidget(): LiveData<List<Widget>> {
        return attoLocalDataSource.getAllWidget()
    }

    override fun deleteWidget(id: Long) {
        return attoLocalDataSource.deleteWidget(id)
    }

    override suspend fun getUser(email:String): Result<User> {
        return attoRemoteDataSource.getUser(email)
    }

    override suspend fun syncRemoteData(
        context: Context,
        user: User,
        appList: List<App>
    ): Result<List<App>>{
        return attoRemoteDataSource.syncRemoteData(context, user, appList)
    }

    override suspend fun uploadData(context: Context,localAppList : List<App>,email: String) : Result<Boolean> {
        return attoRemoteDataSource.uploadData(context, localAppList, email)
    }

    override suspend fun uploadUser(user: User): Result<Boolean> {
        return attoRemoteDataSource.uploadUser(user)
    }

    override fun getAllTimeZone():LiveData<List<AttoTimeZone>> {
        return attoLocalDataSource.getAllTimeZone()
    }

    override fun deleteTimeZone(id: Long) {
        attoLocalDataSource.deleteTimeZone(id)
    }

    override suspend fun updateAppData() {

        withContext(Dispatchers.Default) {

            val roomApps = withContext(Dispatchers.Default) {
                getAllAppNotLiveData()
            }

            val systemApps = withContext(Dispatchers.Default) {
                attoSystemDataSource.getAllAppNotLiveData()
            }

            withContext(ioDispatcher) {

                if (roomApps.isNullOrEmpty()) {
                    systemApps?.let {

                        for (app in it) {
                            withContext(ioDispatcher){
                                insert(app)
                            }
                        }
                    }

                } else {
                    // 判斷system與room的差異
                    // room有的app就不更新
                    // room沒有的就insert
                    systemApps?.let { systemList ->

                        for (app in systemList) {
                            withContext(Dispatchers.IO) {
//                            if (!roomApps.contains(app)) {
                                if (roomApps.none { it.appLabel == app.appLabel }) {
                                    // room沒有的就insert
                                    insert(app)
                                } else {
                                    // room有的就確認imageUrl是否一樣，若不是就更新
                                    if (app.iconPath != roomApps.first { it.appLabel == app.appLabel }.iconPath) {
                                        updateIconPath(app.appLabel, app.iconPath)
                                    }

                                    if (roomApps.any { it.appLabel == app.appLabel }) {
                                        for (item in roomApps.filter { it.appLabel == app.appLabel }) {
                                            if (!item.installed) {
                                                updateAppInstalled(app.appLabel)
                                            }
                                        }
                                    }
                                }
                            }

                            for (currentApp in roomApps) {
                                // room有system沒有代表已刪除，就從room delete
                                if (systemList.none { it.appLabel == currentApp.appLabel } && currentApp.installed) {
                                    delete(currentApp.packageName)
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    override fun getAllAppNotLiveData(): List<App>? {
        return attoLocalDataSource.getAllAppNotLiveData()
    }

    override fun deleteSpecificLabel(label: String) {
        attoLocalDataSource.deleteSpecificLabel(label)
    }



}
package com.chihwhsu.atto.data.database


import androidx.lifecycle.LiveData
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.AppLockTimer
import com.chihwhsu.atto.data.Event
import kotlinx.coroutines.*

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

    override suspend fun updateAppData() {

        withContext(Dispatchers.Main) {

            val roomApps = withContext(Dispatchers.Default) {
                getAllAppNotLiveData()
            }

            val systemApps = attoSystemDataSource.getAllApps()

            withContext(ioDispatcher) {

                if (roomApps.isNullOrEmpty()) {
                    systemApps.value?.let {
                        for (app in it) {
                            insert(app)
                        }
                    }

                } else {
                    // 判斷system與room的差異
                    // room有的app就不更新
                    // room沒有的就insert
                    systemApps.value?.let { systemList ->

                        for (app in systemList) {
//                            if (!roomApps.contains(app)) {
                            if (roomApps.none { it.appLabel == app.appLabel }) {
                                // room沒有的就insert
                                insert(app)
                            }
                        }

                        for (app in roomApps) {
                            // room有system沒有代表已刪除，就從room delete
//                            if (!systemList.contains(app)) {
                            if (systemList.none { it.appLabel == app.appLabel }) {
                                delete(app.packageName)
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
package com.chihwhsu.atto.data.database.local

import android.content.Context
import androidx.lifecycle.LiveData
import com.chihwhsu.atto.data.*
import com.chihwhsu.atto.data.database.AttoDataSource

class AttoLocalDataSource(private val context: Context) : AttoDataSource {

    override suspend fun insert(app: App) {
        AttoDatabase.getInstance(context).attoDatabaseDao.insert(app)
    }

    override suspend fun insert(event: Event) {
        AttoDatabase.getInstance(context).attoDatabaseDao.insert(event)
    }

    override suspend fun insert(appLockTimer: AppLockTimer) {
        AttoDatabase.getInstance(context).attoDatabaseDao.insert(appLockTimer)
    }

    override fun insert(widget: Widget) {
        AttoDatabase.getInstance(context).attoDatabaseDao.insert(widget)
    }

    override fun insert(timeZone: AttoTimeZone) {
        AttoDatabase.getInstance(context).attoDatabaseDao.insert(timeZone)
    }

    override suspend fun update(app: App) {
        AttoDatabase.getInstance(context).attoDatabaseDao.update(app)
    }

    override fun updateLabel(appName: String, label: String?) {
        AttoDatabase.getInstance(context).attoDatabaseDao.updateLabel(appName, label)
    }

    override fun updateSort(appName: String, sort: Int) {
        AttoDatabase.getInstance(context).attoDatabaseDao.updateSort(appName, sort)
    }

    override suspend fun updateTheme(appName: String, theme: Int?) {
        AttoDatabase.getInstance(context).attoDatabaseDao.updateTheme(appName, theme)
    }

    override fun updateAppInstalled(appName: String) {
        AttoDatabase.getInstance(context).attoDatabaseDao.updateAppInstalled(appName, true)
    }

    override suspend fun updateAppData() {
        TODO("Not yet implemented")
    }

    override fun getAllAppNotLiveData(): List<App>? {
        return AttoDatabase.getInstance(context).attoDatabaseDao.getAllAppNotLiveData()
    }

    override fun deleteSpecificLabel(label: String) {
        AttoDatabase.getInstance(context).attoDatabaseDao.deleteSpecificLabel(label)
    }

    override fun updateIconPath(appName: String, path: String) {
        AttoDatabase.getInstance(context).attoDatabaseDao.updateIconPath(appName, path)
    }

    override suspend fun lockApp(packageName: String) {
        AttoDatabase.getInstance(context).attoDatabaseDao.lockApp(packageName, false)
    }

    override suspend fun unLockAllApp() {
        AttoDatabase.getInstance(context).attoDatabaseDao.unLockAllApp(true, false)
    }

    override fun unLockSpecificLabelApp(label: String) {
        AttoDatabase.getInstance(context).attoDatabaseDao.unLockSpecificLabelApp(label, true)
    }

    override suspend fun delete(packageName: String) {
        AttoDatabase.getInstance(context).attoDatabaseDao.delete(packageName)
    }

    override suspend fun clear() {
        AttoDatabase.getInstance(context).attoDatabaseDao.clear()
    }

    override fun getApp(packageName: String): App? {
        return AttoDatabase.getInstance(context).attoDatabaseDao.getApp(packageName)
    }

    override fun getAllApps(): LiveData<List<App>> {
        return AttoDatabase.getInstance(context).attoDatabaseDao.getAllApps()
    }

    override fun getNoLabelApps(): LiveData<List<App>> {
        return AttoDatabase.getInstance(context).attoDatabaseDao.getNoLabelApps()
    }

    override fun getSpecificLabelApps(label: String): LiveData<List<App>> {
        return AttoDatabase.getInstance(context).attoDatabaseDao.getSpecificLabelApps(label)
    }

    override fun getAllAppsWithoutDock(): LiveData<List<App>> {
        return AttoDatabase.getInstance(context).attoDatabaseDao.getAllAppsWithoutDock()
    }

    override fun getLabelList(): LiveData<List<String>> {
        return AttoDatabase.getInstance(context).attoDatabaseDao.getLabelList()
    }

    override fun getAllEvents(): LiveData<List<Event>> {
        return AttoDatabase.getInstance(context).attoDatabaseDao.getAllEvents()
    }

    override suspend fun deleteEvent(id: Int) {
        AttoDatabase.getInstance(context).attoDatabaseDao.deleteEvent(id)
    }

    override suspend fun getEvent(id: Int): Event? {
        return AttoDatabase.getInstance(context).attoDatabaseDao.getEvent(id)
    }

    override fun getTypeEvent(type: Int): LiveData<List<Event>> {
        return AttoDatabase.getInstance(context).attoDatabaseDao.getTypeEvent(type)
    }

    override suspend fun delayEvent5Minutes(id: Int) {
        val currentEvent = getEvent(id)
        currentEvent?.let {
            val newTime = currentEvent.alarmTime + 5 * 60 * 1000
            AttoDatabase.getInstance(context).attoDatabaseDao.delayEvent5Minutes(id, newTime)
        }
    }

    override fun lockAllApp() {
        AttoDatabase.getInstance(context).attoDatabaseDao.lockAllApp(false, true)
    }

    override fun lockSpecificLabelApp(label: String) {
        AttoDatabase.getInstance(context).attoDatabaseDao.lockSpecificLabelApp(label, false)
    }

    override fun isPomodoroIsExist(): Boolean {
        return AttoDatabase.getInstance(context).attoDatabaseDao.isPomodoroIsExist(
            Event.POMODORO_WORK_TYPE,
            Event.POMODORO_BREAK_TYPE
        )
    }

    override suspend fun deleteTimer(id: Long) {
        AttoDatabase.getInstance(context).attoDatabaseDao.deleteTimer(id)
    }

    override suspend fun deleteAllTimer() {
        AttoDatabase.getInstance(context).attoDatabaseDao.deleteAllTimer()
    }

    override suspend fun updateTimer(remainTime: Long) {
        AttoDatabase.getInstance(context).attoDatabaseDao.updateTimer(remainTime)
    }

    override suspend fun getTimer(packageName: String): AppLockTimer? {
        return AttoDatabase.getInstance(context).attoDatabaseDao.getTimer(packageName)
    }

    override fun getAllTimer(): LiveData<List<AppLockTimer>> {
        return AttoDatabase.getInstance(context).attoDatabaseDao.getAllTimer()
    }

    override fun getAllWidget(): LiveData<List<Widget>> {
        return AttoDatabase.getInstance(context).attoDatabaseDao.getAllWidget()
    }

    override fun deleteWidget(id: Long) {
        return AttoDatabase.getInstance(context).attoDatabaseDao.deleteWidget(id)
    }

    override suspend fun getUser(email: String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun syncRemoteData(
        context: Context,
        user: User,
        appList: List<App>
    ): Result<List<App>> {
        TODO()
    }

    override suspend fun uploadData(context: Context, localAppList: List<App>): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadUser(user: User): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getAllTimeZone():LiveData<List<AttoTimeZone>>{
        return AttoDatabase.getInstance(context).attoDatabaseDao.getAllTimeZone()
    }

    override fun deleteTimeZone(id: Long) {
        AttoDatabase.getInstance(context).attoDatabaseDao.deleteTimeZone(id)
    }

}
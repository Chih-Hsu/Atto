package com.chihwhsu.atto.data.database.remote

import android.content.Context
import androidx.lifecycle.LiveData
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.AppLockTimer
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.data.Widget
import com.chihwhsu.atto.data.database.AttoDataSource

object AttoRemoteDataSource : AttoDataSource {

    override suspend  fun insert(app: App) {
        TODO("Not yet implemented")
    }

    override suspend  fun insert(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend  fun insert(appLockTimer: AppLockTimer) {
        TODO("Not yet implemented")
    }

    override fun insert(widget: Widget) {
        TODO("Not yet implemented")
    }

    override suspend  fun update(app: App) {
        TODO("Not yet implemented")
    }

    override fun updateLabel(appName: String, label: String?) {
        TODO("Not yet implemented")
    }

    override fun updateSort(appName: String, sort: Int) {
        TODO("Not yet implemented")
    }

    override suspend  fun updateTheme(appName: String, theme: Int?) {
        TODO("Not yet implemented")
    }

    override suspend  fun lockApp(packageName: String) {
        TODO("Not yet implemented")
    }

    override suspend  fun unLockAllApp() {
        TODO("Not yet implemented")
    }

    override fun unLockSpecificLabelApp(label: String) {
        TODO("Not yet implemented")
    }

    override suspend  fun delete(packageName: String) {
        TODO("Not yet implemented")
    }

    override suspend  fun clear() {
        TODO("Not yet implemented")
    }

    override fun getApp(packageName: String): App? {
        TODO("Not yet implemented")
    }

    override fun getAllApps(): LiveData<List<App>> {
        TODO("Not yet implemented")
    }

    override fun getNoLabelApps(): LiveData<List<App>> {
        TODO("Not yet implemented")
    }

    override fun getSpecificLabelApps(label: String): LiveData<List<App>> {
        TODO("Not yet implemented")
    }

    override fun getAllAppsWithoutDock(): LiveData<List<App>> {
        TODO("Not yet implemented")
    }

    override fun getLabelList(): LiveData<List<String>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAppData() {
        TODO("Not yet implemented")
    }

    override fun getAllAppNotLiveData(): List<App>? {
        TODO("Not yet implemented")
    }

    override fun deleteSpecificLabel(label: String) {
        TODO("Not yet implemented")
    }

    override fun getAllEvents(): LiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend  fun deleteEvent(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend  fun getEvent(id: Int): Event {
        TODO("Not yet implemented")
    }

    override fun getTypeEvent(type: Int): LiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend  fun delayEvent5Minutes(id: Int) {
        TODO("Not yet implemented")
    }

    override fun lockAllApp() {
        TODO("Not yet implemented")
    }

    override fun lockSpecificLabelApp(label: String) {
        TODO("Not yet implemented")
    }

    override fun isPomodoroIsExist(): Boolean {
        TODO("Not yet implemented")
    }


    override suspend  fun deleteTimer(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend  fun deleteAllTimer() {
        TODO("Not yet implemented")
    }

    override suspend  fun updateTimer(remainTime: Long) {
        TODO("Not yet implemented")
    }

    override suspend  fun getTimer(packageName: String): AppLockTimer? {
        TODO("Not yet implemented")
    }

    override fun getAllTimer(): LiveData<List<AppLockTimer>> {
        TODO("Not yet implemented")
    }

    override fun getAllWidget():LiveData<List<Widget>> {
        TODO("Not yet implemented")
    }

    override fun deleteWidget(id: Long) {
        TODO("Not yet implemented")
    }
}
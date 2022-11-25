package com.chihwhsu.atto.data.database.system

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chihwhsu.atto.AttoApplication
import com.chihwhsu.atto.data.*
import com.chihwhsu.atto.data.database.AttoDataSource
import com.chihwhsu.atto.ext.convertToBitmap
import java.io.IOException

class AttoSystemDataSource(val context: Context) : AttoDataSource {

    override suspend fun insert(app: App) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(appLockTimer: AppLockTimer) {
        TODO("Not yet implemented")
    }

    override fun insert(widget: Widget) {
        TODO("Not yet implemented")
    }

    override suspend fun update(app: App) {
        TODO("Not yet implemented")
    }

    override fun updateLabel(appName: String, label: String?) {
        TODO("Not yet implemented")
    }

    override fun updateSort(appName: String, sort: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTheme(appName: String, theme: Int?) {
        TODO("Not yet implemented")
    }

    override fun updateAppInstalled(appName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun lockApp(packageName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun unLockAllApp() {
        TODO("Not yet implemented")
    }

    override fun unLockSpecificLabelApp(label: String) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(packageName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun getApp(packageName: String): App? {
        TODO("Not yet implemented")
    }

    override fun getAllApps(): LiveData<List<App>> {

        val intent = Intent(Intent.ACTION_MAIN, null)
        val currentAppList = mutableListOf<App>()
        val allApps = MutableLiveData<List<App>>()

        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val manager = context.packageManager
        val untreatedAppList: List<ResolveInfo> = manager.queryIntentActivities(intent, 0)

        for (app in untreatedAppList) {
            val appName = app.activityInfo.loadLabel(manager).toString()
            val appPackageName = app.activityInfo.packageName
            val appImage = app.activityInfo.loadIcon(manager)

            val category =
            ApplicationInfo.getCategoryTitle(AttoApplication.instance.applicationContext,app.activityInfo.applicationInfo.category) ?: null

            val newCategory = if (category == null){
                null
            }else{
                category.toString()
            }


            saveFile(appName,appImage.convertToBitmap())
            val newApp = App(appName, appPackageName, context.filesDir.absolutePath +"/"+"$appName.png", label = newCategory?.split(" ")?.first())
            currentAppList.add(newApp)
        }

        allApps.value = currentAppList

        return allApps
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

    override fun updateIconPath(appName: String, path: String) {
        TODO("Not yet implemented")
    }

    override fun getAllEvents(): LiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEvent(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getEvent(id: Int): Event? {
        TODO("Not yet implemented")
    }

    override fun getTypeEvent(type: Int): LiveData<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun delayEvent5Minutes(id: Int) {
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

    override suspend fun deleteTimer(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTimer() {
        TODO("Not yet implemented")
    }

    override suspend fun updateTimer(remainTime: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getTimer(packageName: String): AppLockTimer? {
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

    override suspend fun getUser(email : String): Result<User> {
        TODO("Not yet implemented")
    }

    override suspend fun syncRemoteData(
        context: Context,
        user: User,
        appList: List<App>
    ): Result<List<App>> {
        TODO()
    }

    override suspend fun uploadData(context: Context, localAppList : List<App>) :Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadUser(user: User): Result<Boolean> {
        TODO("Not yet implemented")
    }

    private fun saveFile(filename: String, icon: Bitmap): Boolean {

        return try {
            AttoApplication.instance.applicationContext.openFileOutput(
                "$filename.png",
                Context.MODE_PRIVATE
            ).use { stream ->
                if (!icon.compress(Bitmap.CompressFormat.PNG, 95, stream)) {
                    throw IOException("Couldn't save bitmap.")
                }
                stream.close()
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

}
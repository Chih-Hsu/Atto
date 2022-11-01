package com.chihwhsu.atto.tutorial2_dock

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.ext.convertToBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class DockViewModel(private val packageManager: PackageManager, val resources: Resources, val database: AttoDatabaseDao) : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _appList = MutableLiveData<List<App>>()
    val appList: LiveData<List<App>> get() = _appList

    private var _dockAppList = MutableLiveData<List<App>>()
    val dockAppList: LiveData<List<App>> get() = _dockAppList



    private val originalList = mutableListOf<App>()

    init {
        getAppList()
    }

    // get system app list
    private fun getAppList() {
        val intent = Intent(Intent.ACTION_MAIN, null)
        val currentAppList = mutableListOf<App>()
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val untreatedAppList: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)

        for (app in untreatedAppList) {
            val appName = app.activityInfo.loadLabel(packageManager).toString()
            val appPackageName = app.activityInfo.packageName
            val appImage = app.activityInfo.loadIcon(packageManager)
            val appAdaptiveIcon = packageManager.getDrawable(
                appPackageName,
                app.iconResource,
                app.activityInfo.applicationInfo
            )


            val newApp = App(appName, appPackageName, appImage.convertToBitmap())
            currentAppList.add(newApp)
            coroutineScope.launch(Dispatchers.IO) {
                database.insert(newApp)
            }
        }
        _appList.value = currentAppList
        originalList.addAll(currentAppList)
    }



    fun selectApp(appLabel: String) {
        val allAppList = appList.value
        val dockAppList = mutableListOf<App>().also { newList ->
            dockAppList.value?.let {
                newList.addAll(it)
            }
        }
        allAppList?.let { appList ->
            dockAppList.let { dockList ->

                for (app in appList) {
                    if (app.appLabel == appLabel && !dockList.contains(app) && dockAppList.size < 5) {
                        dockList.add(app)
                        _dockAppList.value = dockList

                        coroutineScope.launch(Dispatchers.IO) {
                            database.updateLabel(app.appLabel,"dock")
                        }
                    } else if (app.appLabel == appLabel && dockList.contains(app)) {
                        dockList.remove(app)
                        _dockAppList.value = dockList

                        coroutineScope.launch(Dispatchers.IO) {
                            database.updateLabel(app.appLabel,null)
                        }
                    } else {

                    }
                }
            }
        }
    }


    fun filterList(text: String?) {
        if (!text.isNullOrEmpty()) {
            val list = mutableListOf<App>()
            originalList.let {
                for (item in it) {
                    if (item.appLabel.lowercase(Locale.ROOT)
                            .contains(text.lowercase(Locale.ROOT))
                    ) {
                        list.add(item)
                    }
                }
            }
            _appList.value = list
        } else {
            _appList.value = originalList
        }
    }

    fun updateAppLabel(appName :String , label : String){
        coroutineScope.launch(Dispatchers.IO) {
            database.updateLabel(appName, label)
        }
    }
}
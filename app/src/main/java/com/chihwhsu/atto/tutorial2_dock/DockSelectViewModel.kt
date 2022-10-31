package com.chihwhsu.atto.tutorial2_dock

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Outline
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import java.util.*

class DockSelectViewModel(val applicationContext: Context) : ViewModel() {

    private var _appList = MutableLiveData<List<App>>()
    val appList : LiveData<List<App>> get() = _appList

    private var _dockAppList = MutableLiveData<List<App>>()
    val dockAppList : LiveData<List<App>> get() = _dockAppList

    private var _queryText = MutableLiveData<String>()
    val queryText : LiveData<String> get() = _queryText

    val originalList = mutableListOf<App>()

    init {
        getAppList()
    }

    // get system app list
    private fun getAppList(){
        val intent = Intent(Intent.ACTION_MAIN,null)
        val currentAppList = mutableListOf<App>()
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val untreatedAppList:List<ResolveInfo> = applicationContext.packageManager.queryIntentActivities(intent,0)

        for (app in untreatedAppList){
            val appName = app.activityInfo.loadLabel(applicationContext.applicationContext.packageManager).toString()
            val appPackageName = app.activityInfo.packageName
            val appImage = app.activityInfo.loadIcon(applicationContext.applicationContext.packageManager)
            val appAdaptiveIcon = applicationContext.applicationContext.packageManager.getDrawable(appPackageName,app.iconResource,app.activityInfo.applicationInfo)

            val newApp = App(appName,appPackageName,appAdaptiveIcon)
            currentAppList.add(newApp)
        }

        _appList.value = currentAppList
        originalList.addAll(currentAppList)

    }

    // check icon is drawable or AdaptiveIconDrawable
    private fun reRenderIcon(image: Drawable?): Drawable? {
        if (image is AdaptiveIconDrawable){
            return image.background
        }else{
            return image
        }

    }

    fun selectApp(appLabel : String){
        val allAppList = appList.value
        val dockAppList = mutableListOf<App>().also { newList ->
            dockAppList.value?.let {
                newList.addAll(it)
            }
            }

        allAppList?.let { appList ->
            dockAppList?.let {  dockList ->

                for (app in appList){
                    if (app.appLabel == appLabel && !dockList.contains(app) && dockAppList.size <5){
                        dockList.add(app)
                        _dockAppList.value = dockList
                    } else if (app.appLabel == appLabel && dockList.contains(app)){
                        dockList.remove(app)
                        _dockAppList.value = dockList
                    } else {

                    }
                }


            }

        }
    }


    fun filterList(text : String?){
        if (!text.isNullOrEmpty()){
        val list = mutableListOf<App>()
            originalList.let {
            for (item in it){
                if (item.appLabel.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))){
                    list.add(item)
                }
            }
        }
        _appList.value = list
        }else{
                _appList.value = originalList
            }

        }
    }

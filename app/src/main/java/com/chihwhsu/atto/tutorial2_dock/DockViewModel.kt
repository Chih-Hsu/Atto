package com.chihwhsu.atto.tutorial2_dock

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.ext.convertToBitmap
import kotlinx.coroutines.*
import java.util.*

class DockViewModel(
    private val packageManager: PackageManager,
    val resources: Resources,
    val repository: AttoRepository
) : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val dataList = repository.getAllApps()
    val dockList = repository.getSpecificLabelApps("dock")

    private var _appList = MutableLiveData<List<App>>()
    val appList: LiveData<List<App>> get() = _appList

    private var _dockAppList = MutableLiveData<List<App>>()
    val dockAppList: LiveData<List<App>> get() = _dockAppList

    private var originalList = listOf<App>()

    init {
//          getAppList()
    }


    fun selectApp(appLabel: String) {

        val allAppList = appList.value

        val dockAppList = mutableListOf<App>().also { newList ->
            dockAppList.value?.let {
                newList.addAll(it)
            }
        }



        allAppList?.let {  apps ->
        dockAppList.let {  dockApps ->


            val currentApp =  apps.filter { it.appLabel == appLabel }.first()
                if ( dockApps.size < 5 && dockApps.filter { it.appLabel == appLabel }.isEmpty()) {

                    dockApps.add(currentApp)
                        _dockAppList.value = dockApps

                        coroutineScope.launch(Dispatchers.IO) {
                            repository.updateLabel(appLabel,"dock")
                            repository.updateSort(appLabel,dockApps.indexOf(currentApp))
                        }
                    } else if ( !dockApps.filter { it.appLabel == appLabel }.isEmpty()) {

                        dockApps.remove(currentApp)
                        _dockAppList.value = dockApps
                    Log.d("dock","$dockApps")

                        coroutineScope.launch(Dispatchers.IO) {
                            repository.updateLabel(appLabel,null)
                            repository.updateSort(appLabel,-1)
                        }
                    } else {

                    Log.d("dock","Hiii")

                    }
                }
            }
        }





//    fun selectApp(appLabel: String) {
//        val allAppList = appList.value
//
//        val dockAppList = mutableListOf<App>().also { newList ->
//            dockAppList.value?.let {
//                newList.addAll(it)
//            }
//        }
//        allAppList?.let { appList ->
//            dockAppList.let { dockList ->
//
//                for (app in appList) {
//                    if (app.appLabel == appLabel && !dockList.contains(app) && dockAppList.size < 5) {
//                        dockList.add(app)
//                        _dockAppList.value = dockList
//                        Log.d("dock","${_dockAppList.value} , $dockList")
//
//                        coroutineScope.launch(Dispatchers.IO) {
//                            repository.updateLabel(appLabel,"dock")
//                            repository.updateSort(appLabel,dockList.indexOf(app))
//                        }
//                    } else if (app.appLabel == appLabel && dockList.contains(app)) {
//                        dockList.remove(app)
//                        _dockAppList.value = dockList
//
//                        coroutineScope.launch(Dispatchers.IO) {
//                            repository.updateLabel(appLabel,null)
//                            repository.updateSort(appLabel,-1)
//                        }
//                    } else {
//
//                    }
//                }
//            }
//        }
//    }

    fun setAppList(list: List<App>) {
        if (appList.value.isNullOrEmpty()) {
            _appList.value = list
            originalList = list
        }
    }

    fun setDockList( list:List<App>){
        if (dockAppList.value.isNullOrEmpty()) {
            _dockAppList.value = list
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


}
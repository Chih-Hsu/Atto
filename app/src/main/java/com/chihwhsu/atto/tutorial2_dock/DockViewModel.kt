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

class DockViewModel(private val packageManager: PackageManager, val resources: Resources, val repository: AttoRepository) : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _appList = MutableLiveData<List<App>>()
    val appList: LiveData<List<App>> get() = _appList

    val dataList = repository.getAllApps()

//    private var _filterList

    private var _dockAppList = MutableLiveData<List<App>>()
    val dockAppList: LiveData<List<App>> get() = _dockAppList



    private var originalList = listOf<App>()

    init {
          getAppList()
    }


    // get system app list
    private fun getAppList() {

        coroutineScope.launch(Dispatchers.Main) {
            val data = repository.getAllApps()
//            data.value?.let {
//                _appList.value = data.value
//                originalList = it
//            }

        }
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
                        Log.d("dock","${_dockAppList.value} , $dockList")

                        coroutineScope.launch(Dispatchers.IO) {
                            repository.updateLabel(appLabel,"dock")
                            repository.updateSort(appLabel,dockList.indexOf(app))
                        }
                    } else if (app.appLabel == appLabel && dockList.contains(app)) {
                        dockList.remove(app)
                        _dockAppList.value = dockList

                        coroutineScope.launch(Dispatchers.IO) {
                            repository.updateLabel(appLabel,null)
                            repository.updateSort(appLabel,-1)
                        }
                    } else {

                    }
                }
            }
        }
    }

    fun setAppList(list: List<App>){
        if (appList.value.isNullOrEmpty()) {
            _appList.value = list
            originalList = list
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
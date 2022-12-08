package com.chihwhsu.atto.setting.dock

import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.database.AttoRepository
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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
    val dockList = repository.getSpecificLabelApps(DOCK)

    private var _appList = MutableLiveData<List<App>>()
    val appList: LiveData<List<App>> get() = _appList

    private var _dockAppList = MutableLiveData<List<App>>()
    val dockAppList: LiveData<List<App>> get() = _dockAppList

    private var originalList = listOf<App>()

    init {
        coroutineScope.launch(Dispatchers.IO) {
            repository.updateAppData()
        }
    }

    fun selectApp(appLabel: String) {

        val allAppList = appList.value

        // if dockList is not null , add all app in dockList
        val dockAppList = mutableListOf<App>().also { newList ->
            dockAppList.value?.let {
                newList.addAll(it)
            }
        }

        allAppList?.let { apps ->
            dockAppList.let { dockApps ->

                // Get first app in filter list,
                // Because appLabel is unique, so only have an app or empty
                val currentApp = apps.first { it.appLabel == appLabel }

                if (dockApps.size < 5 && dockApps.none { it.appLabel == appLabel }) {
                    dockApps.add(currentApp)
                    _dockAppList.value = dockApps

                    updateLabelAndSort(appLabel, DOCK, dockApps.indexOf(currentApp))

                } else if (dockApps.any { it.appLabel == appLabel }) {
                    dockApps.remove(currentApp)
                    _dockAppList.value = dockApps

                    updateLabelAndSort(appLabel, null, DEFAULT_SORT)

                } else {
                    // If dockApps is more than 5 app, don't do anything
                }
            }
        }
    }

    private fun updateLabelAndSort(
        appLabel: String,
        label: String?,
        sort: Int
    ) = coroutineScope.launch(Dispatchers.IO) {
        repository.updateLabel(appLabel, label)
        repository.updateSort(appLabel, sort)
    }


    fun setAppList(list: List<App>) {
        if (appList.value.isNullOrEmpty()) {
            _appList.value = list
            originalList = list
        }
    }

    fun setDockList(list: List<App>) {
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

    companion object {
        private const val DOCK = "dock"
        private const val DEFAULT_SORT = -1
    }
}

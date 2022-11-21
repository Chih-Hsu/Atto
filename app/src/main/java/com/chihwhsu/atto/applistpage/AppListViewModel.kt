package com.chihwhsu.atto.applistpage

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.AppListItem
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.data.database.AttoRepository
import kotlinx.coroutines.*

class AppListViewModel(private val repository: AttoRepository) : ViewModel() {

    val appList = repository.getAllAppsWithoutDock()

    val isHide = mutableMapOf<String, Boolean>()

    private var _appGroupList = MutableLiveData<List<AppListItem>>()
    val appGroupList: LiveData<List<AppListItem>> get() = _appGroupList

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun resetList(appList: List<App>, context: Context) {
//        fun resetList(appList: List<App>, context: Context): List<AppListItem> {
        coroutineScope.launch(Dispatchers.Default) {
            // Get all label
            val labelStringList = mutableListOf<String>()

            appList.forEach {
                if (!labelStringList.contains(it.label)) {
                    labelStringList.add(it.label!!)
                }
            }

            val newList = mutableListOf<AppListItem>()

            for (label in labelStringList) {

                val list = appList.filter { it.label == label }.map { AppListItem.AppItem(it) }
                list.sortedBy { it.app.sort }
                var totalAppUsage = 0L

                for (item in list) {
                    totalAppUsage += item.app.getTodayUsage(context)
                }

                val labelItem = AppListItem.LabelItem(label, totalAppUsage)
                newList.add(labelItem)
                newList.addAll(list)
            }


            withContext(Dispatchers.Main) {
                _appGroupList.value = newList
            }

        }
    }

}
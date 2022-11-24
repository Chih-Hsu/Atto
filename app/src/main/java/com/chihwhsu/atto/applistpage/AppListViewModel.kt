package com.chihwhsu.atto.applistpage

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.AppListItem
import com.chihwhsu.atto.data.database.AttoRepository
import kotlinx.coroutines.*

class AppListViewModel(val repository: AttoRepository) : ViewModel() {

    val appList = repository.getAllAppsWithoutDock()

    val isHide = mutableMapOf<String, Boolean>()

    private var _appGroupList = MutableLiveData<List<AppListItem>>()
    val appGroupList: LiveData<List<AppListItem>> get() = _appGroupList

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun resetList(appList: List<App>, context: Context) {
        coroutineScope.launch(Dispatchers.Default) {

            val labelStringList = getLabelList(appList)

            val newList = mutableListOf<AppListItem>()
            for (label in labelStringList) {

                val list = appList.filter { it.label == label }.map { AppListItem.AppItem(it) }
                list.sortedBy { it.app.sort }

                val totalAppUsage = sumAppUsage(list, context)

                val labelItem = AppListItem.LabelItem(label, totalAppUsage)
                newList.add(labelItem)
                newList.addAll(list)
            }

            withContext(Dispatchers.Main) {
                _appGroupList.value = newList
            }

        }
    }

    private fun sumAppUsage(
        list: List<AppListItem.AppItem>,
        context: Context
    ): Long {
        var totalAppUsage = 0L
        for (item in list) {
            totalAppUsage += item.app.getTodayUsage(context)
        }
        return totalAppUsage
    }

    private fun getLabelList(appList: List<App>): MutableList<String> {
        // Get all label
        val labelStringList = mutableListOf<String>()

        appList.forEach {
            if (!labelStringList.contains(it.label)) {
                labelStringList.add(it.label!!)
            }
        }
        return labelStringList
    }

}
package com.chihwhsu.atto.tutorial3_sort

import android.content.Context
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.AppListItem
import com.chihwhsu.atto.data.database.AttoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SortViewModel(private val repository: AttoRepository) : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val appList = repository.getAllAppsWithoutDock()

    fun resetList(appList : List<App>,context:Context):List<AppListItem>{

        // Get all label
        val labelStringList = mutableListOf<String>()

        appList.forEach {
            if (!labelStringList.contains(it.label)){
                labelStringList.add(it.label!!)
            }
        }


        val newList = mutableListOf<AppListItem>()


        for (label in labelStringList){

            val list = appList.filter { it.label == label }.map { AppListItem.AppItem(it) }

            val totalAppUsage = 0L
            for (item in list){
                totalAppUsage + item.app.getTodayUsage(context)
            }

            val labelItem = AppListItem.LabelItem(label,totalAppUsage)
            newList.add(labelItem)
            newList.addAll(list)
        }

//        Log.d("select","$newList")

        return newList

    }

    fun deleteLabel(label : String){
        coroutineScope.launch(Dispatchers.Default){
            repository.deleteSpecificLabel(label)
        }

    }
}
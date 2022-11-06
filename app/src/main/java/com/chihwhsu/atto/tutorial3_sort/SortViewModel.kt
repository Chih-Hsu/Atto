package com.chihwhsu.atto.tutorial3_sort

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.AppListItem
import com.chihwhsu.atto.data.database.AttoDatabaseDao

class SortViewModel(val databaseDao: AttoDatabaseDao) : ViewModel() {

    val appList = databaseDao.getAllAppsWithoutDock()


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
}
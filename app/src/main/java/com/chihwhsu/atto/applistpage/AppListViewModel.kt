package com.chihwhsu.atto.applistpage

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.AppListItem
import com.chihwhsu.atto.data.database.AttoDatabaseDao

class AppListViewModel(val databaseDao: AttoDatabaseDao) : ViewModel() {

    val appList = databaseDao.getAllAppsWithoutDock()

    fun resetList(appList : List<App> , context: Context):List<AppListItem>{



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
            list.sortedBy { it.app.sort }
            var totalAppUsage = 0L
            for (item in list){
                totalAppUsage  +=  item.app.getUsage(context)
            }

            val labelItem = AppListItem.LabelItem(label,totalAppUsage)
            newList.add(labelItem)
            newList.addAll(list)
        }

//        Log.d("select","$newList")

        return newList

    }

    val isHide = mutableMapOf<String,Boolean>()




}
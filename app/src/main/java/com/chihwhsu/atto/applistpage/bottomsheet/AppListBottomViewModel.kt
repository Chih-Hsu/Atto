package com.chihwhsu.atto.applistpage.bottomsheet

import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.AppListItem
import com.chihwhsu.atto.data.database.AttoDatabaseDao

class AppListBottomViewModel(val databaseDao: AttoDatabaseDao) : ViewModel() {

    val appList = databaseDao.getAllApps()




    fun resetList(appList : List<App>):List<AppListItem>{

        return appList.map { AppListItem.AppItem(it) }
    }
}
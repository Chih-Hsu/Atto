package com.chihwhsu.atto.applistpage.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.AppListItem
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import java.util.*

class AppListBottomViewModel(val databaseDao: AttoDatabaseDao) : ViewModel() {

    var appList = databaseDao.getAllApps()

    private var _filterList = MutableLiveData<List<App>>()
    val filterList : LiveData<List<App>> get() = _filterList


    private var originalList = mutableListOf<App>()


    fun getData(){
        appList.value?.let {
            _filterList.value = it
            originalList.addAll(it)
        }
    }

    fun toAppListItem(appList : List<App>):List<AppListItem>{
        return appList.map { AppListItem.AppItem(it) }.sortedBy { it.app.appLabel.first()}
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
            _filterList.value = list
        } else {
            _filterList.value = originalList
        }
    }
}
package com.chihwhsu.atto.applistpage.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.AppListItem
import com.chihwhsu.atto.data.database.AttoRepository
import java.util.*

class AppListBottomViewModel(private val repository: AttoRepository) : ViewModel() {

    var appList = repository.getAllApps()

    private var _filterList = MutableLiveData<List<App>>()
    val filterList: LiveData<List<App>> get() = _filterList

    private var originalList = mutableListOf<App>()

    fun getData() {
        appList.value?.let {
            _filterList.value = it
            originalList.addAll(it)
        }
    }

    fun toAppListItem(appList: List<App>): List<AppListItem> {
        return appList.filter { it.appLabel != "Atto" }
            .map { AppListItem.AppItem(it) }
            .sortedBy { it.app.appLabel.first() }
    }

    fun filterList(text: String?) {

        if (!text.isNullOrEmpty()) {

            _filterList.value = originalList.filter { it.appLabel.lowercase().contains(text.lowercase()) }
        } else {
            _filterList.value = originalList
        }
    }
}
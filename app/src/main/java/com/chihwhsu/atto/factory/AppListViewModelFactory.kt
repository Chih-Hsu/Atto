package com.chihwhsu.atto.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.applistpage.AppListViewModel
import com.chihwhsu.atto.data.database.AttoDatabaseDao

class AppListViewModelFactory(val databaseDao: AttoDatabaseDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppListViewModel::class.java)){
            return AppListViewModel(databaseDao) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }


}
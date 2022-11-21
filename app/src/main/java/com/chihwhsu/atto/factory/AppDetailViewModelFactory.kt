package com.chihwhsu.atto.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.appdetail.AppDetailViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.database.AttoDatabaseDao

class AppDetailViewModelFactory(val databaseDao: AttoDatabaseDao,val argument : App) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppDetailViewModel::class.java)){
            return AppDetailViewModel(databaseDao,argument) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
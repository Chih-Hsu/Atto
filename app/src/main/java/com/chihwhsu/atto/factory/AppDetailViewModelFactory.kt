package com.chihwhsu.atto.factory

import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.appdetail.AppDetailViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.tutorial2_dock.DockViewModel

class AppDetailViewModelFactory(val databaseDao: AttoDatabaseDao,val argument : App) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppDetailViewModel::class.java)){
            return AppDetailViewModel(databaseDao,argument) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
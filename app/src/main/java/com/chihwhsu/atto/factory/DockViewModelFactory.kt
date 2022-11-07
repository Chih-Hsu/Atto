package com.chihwhsu.atto.factory

import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.tutorial2_dock.DockViewModel

class DockViewModelFactory(val packageManager: PackageManager,val resources: Resources , val database: AttoDatabaseDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DockViewModel::class.java)){
            return DockViewModel(packageManager,resources , database) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
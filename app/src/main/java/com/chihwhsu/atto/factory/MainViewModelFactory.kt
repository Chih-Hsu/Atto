package com.chihwhsu.atto.factory

import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.SettingViewModel

class MainViewModelFactory(val packageManager: PackageManager,val resources: Resources) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)){
            return SettingViewModel(packageManager,resources) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
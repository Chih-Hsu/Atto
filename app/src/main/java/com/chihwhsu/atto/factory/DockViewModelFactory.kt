package com.chihwhsu.atto.factory

import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.setting.dock.DockViewModel

class DockViewModelFactory(
    val packageManager: PackageManager,
    val resources: Resources,
    val repository: AttoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DockViewModel::class.java)) {
            return DockViewModel(packageManager, resources, repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}

package com.chihwhsu.atto.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.tutorial1_wallpaper.WallpaperViewModel
import com.chihwhsu.atto.tutorial2_dock.DockSelectViewModel

class DockSelectViewModelFactory(val applicationContext: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DockSelectViewModel::class.java)) {
            return DockSelectViewModel(applicationContext) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
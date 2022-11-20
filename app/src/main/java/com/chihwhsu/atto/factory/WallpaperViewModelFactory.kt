package com.chihwhsu.atto.factory

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.tutorial.wallpaper.WallpaperViewModel

class WallpaperViewModelFactory(val resources: Resources) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WallpaperViewModel::class.java)){
            return WallpaperViewModel(resources) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
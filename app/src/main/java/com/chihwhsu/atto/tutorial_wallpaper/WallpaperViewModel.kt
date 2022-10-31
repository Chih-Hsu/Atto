package com.chihwhsu.atto.tutorial_wallpaper

import android.content.res.Resources
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.Wallpaper

class WallpaperViewModel(val resource :Resources) : ViewModel() {

    private var _wallpapers = MutableLiveData<List<Wallpaper>>()
    val wallpapers : LiveData<List<Wallpaper>> get() = _wallpapers

    init {
        val wallpaperList = mutableListOf<Wallpaper>(Wallpaper(1,ResourcesCompat.getDrawable(resource,
            R.drawable.wallpaper_plant,null)!!),
            Wallpaper(1,ResourcesCompat.getDrawable(resource,
                R.drawable.wallpaper_fish,null)!!),
            Wallpaper(1,ResourcesCompat.getDrawable(resource,
                R.drawable.wallpaper_black,null)!!))

        _wallpapers.value = wallpaperList
    }
}
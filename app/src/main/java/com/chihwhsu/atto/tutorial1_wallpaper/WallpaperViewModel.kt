package com.chihwhsu.atto.tutorial1_wallpaper

import android.content.res.Resources
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.Wallpaper

class WallpaperViewModel(val resource: Resources) : ViewModel() {

    private var _wallpapers = MutableLiveData<List<Wallpaper>>()
    val wallpapers: LiveData<List<Wallpaper>> get() = _wallpapers

    private var _navigationToNext = MutableLiveData<Boolean>()
    val navigationToNext: LiveData<Boolean> get() = _navigationToNext

    init {
        val wallpaperList = mutableListOf<Wallpaper>(
            Wallpaper(
                1, ResourcesCompat.getDrawable(
                    resource,
                    R.drawable.wallpaper_plant, null
                )!!
            ),
            Wallpaper(
                1, ResourcesCompat.getDrawable(
                    resource,
                    R.drawable.girlstyle, null
                )!!
            ),
            Wallpaper(
                1, ResourcesCompat.getDrawable(
                    resource,
                    R.drawable.wallpaper_black, null
                )!!
            )
        )

        _wallpapers.value = wallpaperList
    }

    fun navigateToNext() {
        _navigationToNext.value = true
    }

    fun doneNavigateToNext() {
        _navigationToNext.value = false
    }
}
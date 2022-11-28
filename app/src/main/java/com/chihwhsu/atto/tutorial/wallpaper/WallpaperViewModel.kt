package com.chihwhsu.atto.tutorial.wallpaper

import android.app.WallpaperManager
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.Wallpaper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WallpaperViewModel(private val resource: Resources) : ViewModel() {

    private var _wallpapers = MutableLiveData<List<Wallpaper>>()
    val wallpapers: LiveData<List<Wallpaper>> get() = _wallpapers

    private var _navigationToNext = MutableLiveData<Boolean>()
    val navigationToNext: LiveData<Boolean> get() = _navigationToNext

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        initWallpaperResource()
    }

    private fun initWallpaperResource() {
        val wallpaperList = mutableListOf(
            Wallpaper(
                1, ResourcesCompat.getDrawable(
                    resource,
                    R.drawable.wallpaper_plant, null
                )!!
            ),
            Wallpaper(
                1, ResourcesCompat.getDrawable(
                    resource,
                    R.drawable.cliff, null
                )!!
            ),
            Wallpaper(
                1, ResourcesCompat.getDrawable(
                    resource,
                    R.drawable.house, null
                )!!
            ),
            Wallpaper(
                1, ResourcesCompat.getDrawable(
                    resource,
                    R.drawable.sunset, null
                )!!
            ),
            Wallpaper(
                1, ResourcesCompat.getDrawable(
                    resource,
                    R.drawable.blank_tradingcard, null
                )!!
            ),
        )

        _wallpapers.value = wallpaperList
    }

    private fun navigateToNext() {
        _navigationToNext.value = true
    }

    fun doneNavigateToNext() {
        _navigationToNext.value = false
    }

    fun setWallPaper(context: Context, image: Drawable) {
        coroutineScope.launch(Dispatchers.IO) {
            val bitmap = image.toBitmap(image.minimumWidth, image.minimumHeight)
            WallpaperManager.getInstance(context).setBitmap(bitmap)
        }
        navigateToNext()
    }
}
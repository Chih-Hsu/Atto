package com.chihwhsu.atto.tutorial1_wallpaper

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

class WallpaperViewModel(val resource: Resources) : ViewModel() {

    private var _wallpapers = MutableLiveData<List<Wallpaper>>()
    val wallpapers: LiveData<List<Wallpaper>> get() = _wallpapers

    private var _navigationToNext = MutableLiveData<Boolean>()
    val navigationToNext: LiveData<Boolean> get() = _navigationToNext

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

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
                    R.drawable.guraaaa, null
                )!!
            )
        )

        _wallpapers.value = wallpaperList
    }

    private fun navigateToNext() {
        _navigationToNext.value = true
    }

    fun doneNavigateToNext() {
        _navigationToNext.value = false
    }

    fun setWallPaper(context: Context, image : Drawable ,){
        coroutineScope.launch(Dispatchers.IO){
            val bitmap = image.toBitmap(image.minimumWidth,image.minimumHeight)
            WallpaperManager.getInstance(context).setBitmap(bitmap)
        }
        navigateToNext()
    }
}
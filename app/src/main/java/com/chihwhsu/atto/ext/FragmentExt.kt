package com.chihwhsu.atto.ext

import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.fragment.app.Fragment
import com.chihwhsu.atto.AttoApplication
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.database.local.AttoDatabase
import com.chihwhsu.atto.factory.AppDetailViewModelFactory
import com.chihwhsu.atto.factory.DockViewModelFactory
import com.chihwhsu.atto.factory.ViewModelFactory
import com.chihwhsu.atto.factory.WallpaperViewModelFactory


fun Fragment.getVmFactory(resources: Resources): WallpaperViewModelFactory {
    return WallpaperViewModelFactory(resources)
}

fun Fragment.getVmFactory(): ViewModelFactory{
    val repository = (requireContext().applicationContext as AttoApplication).attoRepository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(packageManager: PackageManager): DockViewModelFactory {
    val repository = (requireContext().applicationContext as AttoApplication).attoRepository
    return DockViewModelFactory(packageManager,resources,repository)
}

fun Fragment.getVmFactory(argument : App): AppDetailViewModelFactory {
    val repository = (requireContext().applicationContext as AttoApplication).attoRepository
    return AppDetailViewModelFactory(repository,argument)
}


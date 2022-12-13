package com.chihwhsu.atto.ext

import android.app.Activity
import com.chihwhsu.atto.AttoApplication
import com.chihwhsu.atto.factory.ViewModelFactory

fun Activity.getVmFactory(): ViewModelFactory {
    val repository = (this.applicationContext as AttoApplication).attoRepository
    return ViewModelFactory(repository)
}

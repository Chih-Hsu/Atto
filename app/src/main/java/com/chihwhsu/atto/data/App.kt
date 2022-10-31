package com.chihwhsu.atto.data

import android.graphics.drawable.Drawable

data class App (
    val appLabel : String,
    val packageName : String,
    val icon : Drawable?,
    val label : String? = null,
    val isEnable : Boolean = true,
    val theme : Int = -1,
    val installed : Boolean = true
        ) {
}
package com.chihwhsu.atto.util

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils

fun View.clickAnimation(context: Context, animId: Int) {
    val animation = AnimationUtils.loadAnimation(context, animId)
    this.animation = animation
    animation.start()
}

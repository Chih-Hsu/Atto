package com.chihwhsu.atto.data

import android.appwidget.AppWidgetProviderInfo
import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class Widget(
    val name: String,
    val icon: Drawable?,
    val previewImage: Drawable?,
    val info: AppWidgetProviderInfo,
    val width: Int,
    val height: Int
) {
}
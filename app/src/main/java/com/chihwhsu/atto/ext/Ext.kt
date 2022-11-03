package com.chihwhsu.atto.ext

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import java.text.SimpleDateFormat
import java.util.*


// check icon is drawable or AdaptiveIconDrawable
fun Drawable.convertToBitmap(): Bitmap = this.toBitmap(this.minimumWidth,this.minimumHeight,null)

fun Bitmap.createGrayscale(): Bitmap? {
    val width: Int = this.width
    val height: Int = this.height
    val bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmOut)
    val ma = ColorMatrix()
    ma.setSaturation(0f)
    val paint = Paint()
    paint.colorFilter = ColorMatrixColorFilter(ma)
    canvas.drawBitmap(this, 0f, 0f, paint)
    return bmOut
}


fun Long.toFormat(): String? {
    val hours = this/(1000*60*60)
    val minutes = this/(1000*60) - hours*60
    return "${hours}h${minutes}m"
}

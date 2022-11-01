package com.chihwhsu.atto.ext

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap



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
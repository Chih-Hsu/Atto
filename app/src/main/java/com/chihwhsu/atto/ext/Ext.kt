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

fun Int.formatHour() : String{
    val newHour = if (this > 12){
        (this-12)
    } else {
        this
    }

    return if (newHour < 10){
        "0${newHour}"
    } else{
        "$newHour"
    }
}

fun Int.formatMinutes():String{
    return if (this < 10){
        "0$this"
    } else {
        this.toString()
    }

}

fun getTimeFrom00am(time: Long):Long{
    val today = Calendar.getInstance()
    today[Calendar.MILLISECOND] = 0
    today[Calendar.SECOND] = 0
    today[Calendar.MINUTE] = 0
    today[Calendar.HOUR_OF_DAY] = 0
    today.timeInMillis

    return time - today.timeInMillis
}

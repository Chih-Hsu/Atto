package com.chihwhsu.atto.ext

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
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


fun Float.toDp():Int{
    return (this / Resources.getSystem().displayMetrics.density).toInt()
}


fun getTimeFrom00am(time: Long):Long{
    val todayStart = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    return time - todayStart
}

fun getCurrentDay():Long{
    return LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun getEndOfToday():Long {
    return LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()+ 86400000
}

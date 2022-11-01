package com.chihwhsu.atto.data.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


class AttoConverter {

    @TypeConverter
    fun bitmapToString(bitmap: Bitmap):String?{
        val outPutStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outPutStream)
        val byte = outPutStream.toByteArray()
        val temp = Base64.encodeToString(byte,Base64.DEFAULT)

        if (temp == null){
            return null
        }else{
            return temp
        }
    }

    @TypeConverter
    fun stringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            bitmap
        } catch (e: Exception) {
            e.message
            null
        }
    }


}



package com.chihwhsu.atto.data.database

import android.net.Uri
import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class EventConverter {

    @TypeConverter
    fun convertBooleanListToJson(list: List<Boolean>?): String? {
        list?.let {
            return Moshi.Builder().build().adapter<List<Boolean>>(List::class.java).toJson(list)
        }
        return null
    }


    @TypeConverter
    fun convertJsonToBooleanList(json: String?): List<Boolean>? {
        json?.let {
            val type = Types.newParameterizedType(List::class.java, Boolean::class.javaObjectType)
            val adapter: JsonAdapter<List<Boolean>> = Moshi.Builder().build().adapter(type)

            return adapter.fromJson(json)
        }

        return null

    }
//    @TypeConverter
//    fun convertListToJson(list: List<String>?): String? {
//        list?.let {
//            return Moshi.Builder().build().adapter<List<String>>(List::class.java).toJson(list)
//        }
//        return null
//    }
//
//
//    @TypeConverter
//    fun convertJsonToList(json: String?): List<String>? {
//        json?.let {
//            val type = Types.newParameterizedType(List::class.java, String::class.java)
//            val adapter: JsonAdapter<List<String>> = Moshi.Builder().build().adapter(type)
//            return adapter.fromJson(it)
//        }
//        return null
//    }

    // Uri
    @TypeConverter
    fun convertUriToString(uri: Uri): String {
        return uri.toString()
    }


    @TypeConverter
    fun convertStringToUri(path: String?): Uri? {
        return Uri.parse(path)
    }



}
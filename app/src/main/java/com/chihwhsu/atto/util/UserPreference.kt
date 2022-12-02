package com.chihwhsu.atto.util

import android.content.Context
import com.chihwhsu.atto.AttoApplication


object UserPreference {

    private val userPreference =
        AttoApplication.instance.getSharedPreferences("preference", Context.MODE_PRIVATE)

    var showSingleTimeZoneClock: Boolean
        set(value) {
            userPreference.edit().putBoolean("displayMode", value).apply()
        }
        get() {
            return userPreference.getBoolean("displayMode", false)
        }

    var isHomeFirstTimeLaunch: Boolean
        set(value) {
            userPreference.edit().putBoolean("homeFirstTime", value).apply()
        }
        get() {
            return userPreference.getBoolean("homeFirstTime", true)
        }

    var isListFirstTimeLaunch: Boolean
        set(value) {
            userPreference.edit().putBoolean("listFirstTime", value).apply()
        }
        get() {
            return userPreference.getBoolean("listFirstTime", true)
        }


}
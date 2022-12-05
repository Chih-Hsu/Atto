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

    // Home
    var isHomeFirstTimeLaunch: Boolean
        set(value) {
            userPreference.edit().putBoolean("homeFirstTime", value).apply()
        }
        get() {
            return userPreference.getBoolean("homeFirstTime", true)
        }

    // AppList
    var showLabelAnimation: Boolean
        set(value) {
            userPreference.edit().putBoolean("showLabelAnimation", value).apply()
        }
        get() {
            return userPreference.getBoolean("showLabelAnimation", true)
        }

    var showLongClickAnimation: Boolean
        set(value) {
            userPreference.edit().putBoolean("showLongClickAnimation", value).apply()
        }
        get() {
            return userPreference.getBoolean("showLongClickAnimation", true)
        }

    var showBackgroundSlideAnimation: Boolean
        set(value) {
            userPreference.edit().putBoolean("showBackgroundSlideAnimation", value).apply()
        }
        get() {
            return userPreference.getBoolean("showBackgroundSlideAnimation", true)
        }





}
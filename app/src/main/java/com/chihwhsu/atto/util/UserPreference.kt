package com.chihwhsu.atto.util

import android.content.Context
import com.chihwhsu.atto.AttoApplication

object UserPreference {

    private const val PREFERENCE = "preference"
    private const val DISPLAY_MODE = "displayMode"
    private const val HOME_FIRST_TIME = "homeFirstTime"
    private const val SHOW_LABEL_ANIMATION = "showLabelAnimation"
    private const val SHOW_BACKGROUND_SLIDE_ANIMATION = "showBackgroundSlideAnimation"

    private val userPreference =
        AttoApplication.instance.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)

    var showSingleTimeZoneClock: Boolean
        set(value) {
            userPreference.edit().putBoolean(DISPLAY_MODE, value).apply()
        }
        get() {
            return userPreference.getBoolean(DISPLAY_MODE, false)
        }

    // Home
    var isHomeFirstTimeLaunch: Boolean
        set(value) {
            userPreference.edit().putBoolean(HOME_FIRST_TIME, value).apply()
        }
        get() {
            return userPreference.getBoolean(HOME_FIRST_TIME, true)
        }

    // AppList
    var showLabelAnimation: Boolean
        set(value) {
            userPreference.edit().putBoolean(SHOW_LABEL_ANIMATION, value).apply()
        }
        get() {
            return userPreference.getBoolean(SHOW_LABEL_ANIMATION, true)
        }


    var showBackgroundSlideAnimation: Boolean
        set(value) {
            userPreference.edit().putBoolean(SHOW_BACKGROUND_SLIDE_ANIMATION, value).apply()
        }
        get() {
            return userPreference.getBoolean(SHOW_BACKGROUND_SLIDE_ANIMATION, true)
        }


}

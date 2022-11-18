package com.chihwhsu.atto.util

import android.content.Context
import com.chihwhsu.atto.AttoApplication

object UserManager {

    // 僅限內部使用
    private val userData =
        AttoApplication.instance.getSharedPreferences("userData", Context.MODE_PRIVATE)

    var userToken: String?
        set(value) {
            userData.edit().putString("token", value).apply()
        }
        get() {
            return userData.getString("token", null)
        }

    fun isLogging(): Boolean {
        return userToken != null
    }

    fun logOut() {
        userData.edit().remove("token").apply()
//        LoginManager.getInstance().logOut()
    }
}
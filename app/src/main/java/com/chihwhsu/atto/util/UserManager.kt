package com.chihwhsu.atto.util

import android.content.Context
import com.chihwhsu.atto.AttoApplication

object UserManager {

    // 僅限內部使用
    private val userData =
        AttoApplication.instance.getSharedPreferences("userData", Context.MODE_PRIVATE)

    var userEmail: String?
        set(value) {
            userData.edit().putString("email", value).apply()
        }
        get() {
            return userData.getString("email", null)
        }

    fun isLogging(): Boolean {
        return userEmail != null
    }

    fun logOut() {
        userData.edit().remove("email").apply()
//        LoginManager.getInstance().logOut()
    }
}
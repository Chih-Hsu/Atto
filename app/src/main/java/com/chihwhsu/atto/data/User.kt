package com.chihwhsu.atto.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String? = null,
    val email: String? = null,
    val name: String? = null,
    val image: String? = null,
    val deviceId: String? = null
) : Parcelable

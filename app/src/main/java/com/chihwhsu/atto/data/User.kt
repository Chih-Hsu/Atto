package com.chihwhsu.atto.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val idToken: String? = null,
    val id: String? = null,
    val email: String? = null,
    val name: String? = null,
    val image: String? = null
) : Parcelable {
}
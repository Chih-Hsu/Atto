package com.chihwhsu.atto.data

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val id : String?,
    val email : String?,
    val name : String?,
    val image : Uri?
        ) : Parcelable {
}
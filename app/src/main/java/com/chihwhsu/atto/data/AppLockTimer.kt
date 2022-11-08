package com.chihwhsu.atto.data


// When user set a usage limit for a app
//  record the targetTime and everyTime user start the app
// launch a foreground service to count the usage time
data class AppLockTimer(
    val id:Long = 0,
    val packageName:String,
    val usageTime:Long = 0,
    val targetTime:Long = 0,
) {
}
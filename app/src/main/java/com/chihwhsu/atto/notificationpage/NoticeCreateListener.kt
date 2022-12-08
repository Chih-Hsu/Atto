package com.chihwhsu.atto.notificationpage

interface NoticeCreateListener {

    fun notifyCreate(boolean: Boolean)

    fun cancelCurrentNotification(key: String)
}

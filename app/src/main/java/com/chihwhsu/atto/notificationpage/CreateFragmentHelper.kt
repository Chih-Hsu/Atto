package com.chihwhsu.atto.notificationpage

object CreateFragmentHelper {

    private var newInstance: CreateFragmentHelper? = null
    private var createListener: NoticeCreateListener? = null

    fun getInstance(): CreateFragmentHelper {
        if (newInstance == null) {
            newInstance = this
        }
        return newInstance as CreateFragmentHelper
    }

    fun notifyFragmentCreate(boolean: Boolean) {
        createListener?.notifyCreate(boolean)
    }

    fun setCreateListener(notifyListener: NoticeCreateListener?) {
        this.createListener = notifyListener
    }

    fun cancelNotification(key: String) {
        createListener?.cancelCurrentNotification(key)
    }
}

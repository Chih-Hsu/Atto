package com.chihwhsu.atto.notificationpage

import android.service.notification.StatusBarNotification

object NotifyHelper {

    var newInstance: NotifyHelper? = null
    private var notifyListener: NotifyListener? = null

    fun onReceive(sbn: StatusBarNotification) {
        notifyListener?.onReceiveMessage(sbn)
    }

    fun onRemoved(sbn: StatusBarNotification) {
        notifyListener?.onRemovedMessage(sbn)
    }

    fun setNotifyListener(notifyListener: NotifyListener?) {
        this.notifyListener = notifyListener
    }

    fun setInitNotification(notifications: Array<StatusBarNotification>) {
        val newList = mutableListOf<StatusBarNotification>()
        for (i in notifications) {
            newList.add(i)
        }
        notifyListener?.setInitNotification(newList)
    }

    fun getInstance(): NotifyHelper {
        if (newInstance == null) {
            newInstance = this
        }
        return newInstance as NotifyHelper
    }
}

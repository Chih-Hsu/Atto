package com.chihwhsu.atto.notificationpage

import android.service.notification.StatusBarNotification

interface NotifyListener {

    fun onReceiveMessage(sbn: StatusBarNotification)

    fun onRemovedMessage(sbn: StatusBarNotification)

    fun setInitNotification(notifications: List<StatusBarNotification>)
}

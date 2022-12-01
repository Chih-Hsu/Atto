package com.chihwhsu.atto.notificationpage

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class AttoNotificationListenerService : NotificationListenerService(),NoticeCreateListener {

    init {
        CreateFragmentHelper.getInstance().setCreateListener(this)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        NotifyHelper.getInstance().setInitNotification(activeNotifications)

        sbn?.let {
            NotifyHelper.getInstance().onReceive(it)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        sbn?.let {
            NotifyHelper.getInstance().onRemoved(it)
        }
    }

    override fun notifyCreate(boolean: Boolean) {
        if (boolean){
            NotifyHelper.getInstance().setInitNotification(activeNotifications)
        }
    }

    override fun cancelCurrentNotification(key: String) {
        cancelNotification(key)
    }

}


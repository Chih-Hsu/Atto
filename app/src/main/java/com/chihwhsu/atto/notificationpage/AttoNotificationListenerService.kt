package com.chihwhsu.atto.notificationpage

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class AttoNotificationListenerService : NotificationListenerService() {

    init {
        NotifyHelper.getInstance().setInitNotification(activeNotifications)
    }



    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }


    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        for (i in sbn?.notification?.extras!!.keySet()){
            Log.d("notificationTest"," $i")
        }

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

}
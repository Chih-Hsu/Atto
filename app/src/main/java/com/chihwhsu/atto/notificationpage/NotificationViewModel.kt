package com.chihwhsu.atto.notificationpage

import android.service.notification.StatusBarNotification
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationViewModel : ViewModel() {

    private var _notificationList = MutableLiveData<List<StatusBarNotification>>()
    val notificationList: LiveData<List<StatusBarNotification>> get() = _notificationList

    fun removeNotification(sbn: StatusBarNotification) {
        val list = mutableListOf<StatusBarNotification>()
        notificationList.value?.let {
            list.addAll(it)
        }

        list.removeIf { it.notification.channelId == sbn.notification.channelId }

        _notificationList.value = list
    }

    fun setInitNotification(notifications: List<StatusBarNotification>) {
        _notificationList.value = notifications
    }

    fun remove(sbn: StatusBarNotification) {
        CreateFragmentHelper.getInstance().cancelNotification(sbn.key)
    }
}

package com.chihwhsu.atto.notificationpage

import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationViewModel : ViewModel() {

    private var _notificationList = MutableLiveData<List<StatusBarNotification>>()
    val notificationList : LiveData<List<StatusBarNotification>> get() = _notificationList


    fun receiveNotification(sbn : StatusBarNotification){
        val list = mutableListOf<StatusBarNotification>()
//        notificationList.value?.let {
//            list.addAll(it)
//        }
//
//        for (i in list){
//            if (i.notification.channelId == sbn.notification.channelId){
//                list.remove(i)
//            }
//        }
//        list.add(sbn)
//
//        _notificationList.value = list
    }

    fun removeNotification(sbn : StatusBarNotification){
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


}
package com.chihwhsu.atto.notificationpage

import android.content.Intent
import android.os.Bundle
import android.service.notification.StatusBarNotification
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.databinding.FragmentNotificationBinding


class NotificationFragment : Fragment(), NotifyListener {

    val CHANNEL_ID = "channelId"
    val CHANNEL_NAME = "foreground_service"
    val NOTIFICATION_ID = 1
    val REQUEST_CODE = 999

    private lateinit var adapter: NotificationAdapter
    private lateinit var viewModel: NotificationViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNotificationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)

        // Get NotificationListenerService Data
        NotifyHelper.getInstance().setNotifyListener(this)

        // Notify NotificationListenerService this fragment is create
        CreateFragmentHelper.getInstance().notifyFragmentCreate(true)

        val adapter = NotificationAdapter()
        binding.recyclerviewNotification.adapter = adapter

        viewModel.notificationList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        requestPermission()

//        binding.testButton.setOnClickListener {
//            createNotificationChannel()
//        }

        return binding.root
    }


    private fun requestPermission() {

        if (!isNLServiceEnabled()) {
            startActivityForResult(
                Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"),
                REQUEST_CODE
            )
        } else {
//            Toast.makeText(requireContext(),"通知服务已开启",Toast.LENGTH_SHORT).show()
//            toggleNotificationListenerService()
        }
    }

    private fun isNLServiceEnabled(): Boolean {
        val packageNames = NotificationManagerCompat.getEnabledListenerPackages(requireActivity())

        return packageNames.contains("com.chihwhsu.atto")
    }

//    private fun toggleNotificationListenerService(context: Context) {
//        val pm = context.packageManager
//        pm.setComponentEnabledSetting(
//            ComponentName(context, AttoNotificationListenerService::class.java),
//            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
//        )
//        pm.setComponentEnabledSetting(
//            ComponentName(context, AttoNotificationListenerService::class.java),
//            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
//        )
//    }

    override fun onReceiveMessage(sbn: StatusBarNotification) {

        viewModel.receiveNotification(sbn)
    }

    override fun onRemovedMessage(sbn: StatusBarNotification) {
        viewModel.removeNotification(sbn)
    }

    override fun setInitNotification(notifications: List<StatusBarNotification>) {
        viewModel.setInitNotification(notifications)
    }

}
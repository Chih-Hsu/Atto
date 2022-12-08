package com.chihwhsu.atto.notificationpage

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.service.notification.StatusBarNotification
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment(), NotifyListener {

    private lateinit var adapter: NotificationAdapter
    private lateinit var viewModel: NotificationViewModel
    private lateinit var binding: FragmentNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[NotificationViewModel::class.java]

        // Get NotificationListenerService Data
        NotifyHelper.getInstance().setNotifyListener(this)

        // Notify NotificationListenerService this fragment is create
        CreateFragmentHelper.getInstance().notifyFragmentCreate(true)

        adapter = NotificationAdapter()
        binding.recyclerviewNotification.adapter = adapter

        viewModel.notificationList.observe(
            viewLifecycleOwner
        ) {
            adapter.submitList(it)
        }

        requestPermission()

        setItemTouchHelper()

        return binding.root
    }

    private fun requestPermission() {

        if (!isNLServiceEnabled()) {
            startActivityForResult(
                Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS),
                REQUEST_CODE
            )
        } else {
            // Reset Notification Listener Service
            // If No this function , sometimes it will not work,so need to reset evey time is better
            toggleNotificationListenerService(requireContext())
        }
    }

    private fun isNLServiceEnabled(): Boolean {
        val packageNames = NotificationManagerCompat.getEnabledListenerPackages(requireActivity())

        return packageNames.contains(ATTO)
    }

    private fun toggleNotificationListenerService(context: Context) {
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            ComponentName(context, AttoNotificationListenerService::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
        )
        pm.setComponentEnabledSetting(
            ComponentName(context, AttoNotificationListenerService::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        )
    }

    override fun onReceiveMessage(sbn: StatusBarNotification) {

//        viewModel.receiveNotification(sbn)
    }

    override fun onRemovedMessage(sbn: StatusBarNotification) {
        viewModel.removeNotification(sbn)
    }

    override fun setInitNotification(notifications: List<StatusBarNotification>) {
        viewModel.setInitNotification(notifications)
    }

    private fun setItemTouchHelper() {
        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
            MIN_DISTANCE
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                adapter.notifyItemMoved(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                viewModel.remove(adapter.currentList[position])
            }
        }
        val itemHelper = ItemTouchHelper(simpleCallback)
        itemHelper.attachToRecyclerView(binding.recyclerviewNotification)
    }

    companion object {
        private const val REQUEST_CODE = 999
        private const val ATTO = "com.chihwhsu.atto"
        private const val ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
        private const val MIN_DISTANCE = 100
    }
}

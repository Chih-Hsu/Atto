package com.chihwhsu.atto.notificationpage

import android.app.Notification
import android.service.notification.StatusBarNotification
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.databinding.ItemNotificationBinding

class NotificationAdapter : ListAdapter<StatusBarNotification,NotificationAdapter.NotificationViewHolder>(object :DiffUtil.ItemCallback<StatusBarNotification>(){
    override fun areContentsTheSame(
        oldItem: StatusBarNotification,
        newItem: StatusBarNotification
    ): Boolean {
        return false
    }

    override fun areItemsTheSame(
        oldItem: StatusBarNotification,
        newItem: StatusBarNotification
    ): Boolean {
        return oldItem.id == newItem.id
    }

}) {


    inner class NotificationViewHolder(val binding:ItemNotificationBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(item : StatusBarNotification){

            val extras = item.notification.extras
            binding.textTitle.text = extras.get("android.title").toString()
            binding.textContent.text = extras.get("android.text").toString()
            binding.textSubtitle.text = extras.get("android.subText").toString()

            val icon = item.notification.smallIcon
            binding.imageIcon.setImageDrawable(icon.loadDrawable(itemView.context))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
       val currentItem = getItem(position)
        holder.bind(currentItem)


    }
}
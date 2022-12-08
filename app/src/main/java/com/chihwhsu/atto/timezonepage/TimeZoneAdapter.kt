package com.chihwhsu.atto.timezonepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.AttoTimeZone
import com.chihwhsu.atto.databinding.ItemTimezoneBinding

class TimeZoneAdapter(val parent: Int) : ListAdapter<AttoTimeZone, TimeZoneAdapter.TimeZoneViewHolder>(object : DiffUtil.ItemCallback<AttoTimeZone>() {

    override fun areContentsTheSame(oldItem: AttoTimeZone, newItem: AttoTimeZone): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: AttoTimeZone, newItem: AttoTimeZone): Boolean {
        return oldItem.id == newItem.id
    }
}) {

    inner class TimeZoneViewHolder(val binding: ItemTimezoneBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AttoTimeZone) {
            binding.textTimezoneName.text = item.name.uppercase()
            binding.textclockTimezone.timeZone = item.locale

            // If show on home fragment, change color to white else black
            if (parent == HOME_FRAGMENT) {
                binding.apply {
                    textTimezoneName.setTextColor(ResourcesCompat.getColor(itemView.resources, R.color.light_grey, null))
                    textclockTimezone.setTextColor(ResourcesCompat.getColor(itemView.resources, R.color.light_grey, null))
                }
            } else {
                binding.apply {
                    textTimezoneName.setTextColor(ResourcesCompat.getColor(itemView.resources, R.color.black, null))
                    textclockTimezone.setTextColor(ResourcesCompat.getColor(itemView.resources, R.color.black, null))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeZoneViewHolder {
        val view = ItemTimezoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeZoneViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeZoneViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    companion object {
        const val HOME_FRAGMENT = 1
        const val TIMEZONE_FRAGMENT = 2
    }
}

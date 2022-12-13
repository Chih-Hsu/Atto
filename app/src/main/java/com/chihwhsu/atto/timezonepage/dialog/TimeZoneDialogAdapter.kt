package com.chihwhsu.atto.timezonepage.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.data.AttoTimeZone
import com.chihwhsu.atto.databinding.ItemTimezoneDialogBinding

class TimeZoneDialogAdapter(val onClickListener: TimeZoneClickListener) : ListAdapter<AttoTimeZone, TimeZoneDialogAdapter.TimeZoneViewHolder>(object :
        DiffUtil.ItemCallback<AttoTimeZone>() {

        override fun areContentsTheSame(oldItem: AttoTimeZone, newItem: AttoTimeZone): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: AttoTimeZone, newItem: AttoTimeZone): Boolean {
            return oldItem == newItem
        }
    }) {

    class TimeZoneClickListener(val onClickListener: (AttoTimeZone) -> Unit) {
        fun onClick(timeZone: AttoTimeZone) = onClickListener(timeZone)
    }

    inner class TimeZoneViewHolder(val binding: ItemTimezoneDialogBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AttoTimeZone) {
            binding.textTimezoneId.text = item.name
            itemView.setOnClickListener {
                onClickListener.onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeZoneViewHolder {
        val view = ItemTimezoneDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeZoneViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeZoneViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}

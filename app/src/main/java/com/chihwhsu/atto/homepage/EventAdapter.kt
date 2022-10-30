package com.chihwhsu.atto.homepage

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.*
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.databinding.ItemEventBinding

class EventAdapter(val onClickListener: EventClickListener) :
    ListAdapter<Event, EventAdapter.EventViewHolder>(object : DiffUtil.ItemCallback<Event>() {
        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }
    }) {

    class EventClickListener(val onClickListener: (event: Event) -> Unit) {
        fun onClick(event: Event) = onClickListener(event)
    }

    inner class EventViewHolder(val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {

//            itemView.setOnClickListener {
//                onClickListener.onClick(event)
//            }

            if (event.isDone) {
                binding.eventDone.visibility = View.VISIBLE
                binding.eventDoing.visibility = View.INVISIBLE
                binding.eventDone.drawable.setColorFilter(
                    ResourcesCompat.getColor(
                        itemView.resources,
                        R.color.red,
                        null
                    ), PorterDuff.Mode.SRC_IN
                )
//                binding.eventDone.drawable.colorFilter = BlendModeColorFilter(ResourcesCompat.getColor(itemView.resources,R.color.red,null),BlendMode.SRC_IN)
            } else {
                binding.eventDone.visibility = View.INVISIBLE
                binding.eventDoing.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(currentItem)
        }
        holder.bind(currentItem)
    }

}
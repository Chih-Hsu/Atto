package com.chihwhsu.atto.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.databinding.ItemEventBinding
import com.chihwhsu.atto.ext.getTimeFrom00am

class HomeAdapter(val onClickListener: EventClickListener,val viewModel :HomeViewModel) :
    ListAdapter<Event, HomeAdapter.EventViewHolder>(object : DiffUtil.ItemCallback<Event>() {
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

            itemView.setOnClickListener {
                onClickListener.onClick(event)
            }


            if (getTimeFrom00am(System.currentTimeMillis()) > event.alarmTime) {
                if (event == viewModel.event.value){
                    binding.eventDone.setImageResource(R.drawable.event_done_select)

                }else{
                    binding.eventDone.setImageResource(R.drawable.event_done)

                }
            } else {
                if (event == viewModel.event.value){
                    binding.eventDone.setImageResource(R.drawable.event_doing_select)
                }else{
                    binding.eventDone.setImageResource(R.drawable.event_doing)
                }
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
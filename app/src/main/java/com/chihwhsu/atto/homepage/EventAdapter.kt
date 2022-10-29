package com.chihwhsu.atto.homepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.databinding.ItemEventBinding

class EventAdapter : ListAdapter<Event,EventAdapter.EventViewHolder>(object : DiffUtil.ItemCallback<Event>(){
    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }
}){

    class EventViewHolder(val binding : ItemEventBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(event : Event){
            if (event.isDone){
                binding.eventDone.visibility = View.VISIBLE
                binding.eventDoing.visibility = View.INVISIBLE
            }else{
                binding.eventDone.visibility = View.INVISIBLE
                binding.eventDoing.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = ItemEventBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

}
package com.chihwhsu.atto.homepage


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.data.Event.Companion.ALARM_TYPE
import com.chihwhsu.atto.data.Event.Companion.POMODORO_BREAK_TYPE
import com.chihwhsu.atto.data.Event.Companion.POMODORO_WORK_TYPE
import com.chihwhsu.atto.data.Event.Companion.TODO_TYPE
import com.chihwhsu.atto.databinding.ItemEventBinding
import com.chihwhsu.atto.ext.getTimeFromStartOfDay

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

            val color = when(event.type){
                ALARM_TYPE -> ResourcesCompat.getColor(itemView.resources,R.color.yellow,null)
                TODO_TYPE -> ResourcesCompat.getColor(itemView.resources,R.color.mid_gray,null)
                POMODORO_WORK_TYPE -> ResourcesCompat.getColor(itemView.resources,R.color.red,null)
                POMODORO_BREAK_TYPE -> ResourcesCompat.getColor(itemView.resources,R.color.brown,null)
                else -> ResourcesCompat.getColor(itemView.resources,R.color.light_grey,null)
            }

            binding.eventImage.setColorFilter(color)



            if (getTimeFromStartOfDay(System.currentTimeMillis()) > event.alarmTime) {
                if (event == viewModel.event.value){
                    binding.eventImage.setImageResource(R.drawable.event_done_select)

                }else{
                    binding.eventImage.setImageResource(R.drawable.event_done)

                }
            } else {
                if (event == viewModel.event.value){
                    binding.eventImage.setImageResource(R.drawable.event_doing_select)
                }else{
                    binding.eventImage.setImageResource(R.drawable.event_doing)
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
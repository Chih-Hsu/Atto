package com.chihwhsu.atto.clock.alarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.databinding.ItemAlarmListBinding
import com.chihwhsu.atto.ext.getTimeFromStartOfDay
import com.chihwhsu.atto.ext.toFormat

class AlarmAdapter(
    val onClickListener: AlarmOnClickListener,
    val onCheckChangeListener: AlarmCheckChangeListener
) : ListAdapter<Event, AlarmAdapter.AlarmViewHolder>(object : DiffUtil.ItemCallback<Event>() {
    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }
}) {

    class AlarmOnClickListener(val onClickListener: (event: Event) -> Unit) {
        fun onClick(event: Event) = onClickListener(event)
    }

    class AlarmCheckChangeListener(
        val onCheckChangeListener: (boolean: Boolean, event: Event) -> Unit
    ) {
        fun onCheckChange(boolean: Boolean, event: Event) = onCheckChangeListener(boolean, event)
    }

    inner class AlarmViewHolder(val binding: ItemAlarmListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Event) {
            binding.hourMinute.text = getTimeFromStartOfDay(item.alarmTime).toFormat()
            setBackgroundColor(item)
            setAlarmSwitch(item)

            binding.buttonDelete.setOnClickListener {
                onClickListener.onClick(item)
            }
        }

        private fun setAlarmSwitch(item: Event) {
            binding.switchOpen.isChecked =
                true // Default, every alarm is default checked when create

            binding.switchOpen.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    onCheckChangeListener.onCheckChange(true, item)
                } else {
                    onCheckChangeListener.onCheckChange(false, item)
                }
            }
        }

        private fun setBackgroundColor(item: Event) {
            item.routine?.let {
                binding.monday.setBackgroundResource(if (it[0]) R.drawable.week_corner_background_select else R.drawable.week_corner_background)
                binding.tuesday.setBackgroundResource(if (it[1]) R.drawable.week_corner_background_select else R.drawable.week_corner_background)
                binding.wednesday.setBackgroundResource(if (it[2]) R.drawable.week_corner_background_select else R.drawable.week_corner_background)
                binding.thursday.setBackgroundResource(if (it[3]) R.drawable.week_corner_background_select else R.drawable.week_corner_background)
                binding.friday.setBackgroundResource(if (it[4]) R.drawable.week_corner_background_select else R.drawable.week_corner_background)
                binding.saturday.setBackgroundResource(if (it[5]) R.drawable.week_corner_background_select else R.drawable.week_corner_background)
                binding.sunday.setBackgroundResource(if (it[6]) R.drawable.week_corner_background_select else R.drawable.week_corner_background)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = ItemAlarmListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}

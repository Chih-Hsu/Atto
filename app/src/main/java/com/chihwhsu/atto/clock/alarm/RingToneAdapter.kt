package com.chihwhsu.atto.clock.alarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.applistpage.AppListViewModel
import com.chihwhsu.atto.data.RingTone
import com.chihwhsu.atto.databinding.ItemRingtoneSpinnerBinding


class RingToneAdapter(val appOnClickListener: PlayOnClickListener) :
    ListAdapter<RingTone, RingToneAdapter.RingToneViewHolder>(object :
        DiffUtil.ItemCallback<RingTone>() {
        override fun areItemsTheSame(oldItem: RingTone, newItem: RingTone): Boolean {
            return oldItem.path == newItem.path
        }

        override fun areContentsTheSame(oldItem: RingTone, newItem: RingTone): Boolean {
            return oldItem == newItem
        }
    }) {


    class PlayOnClickListener(val onClickListener: (packageName: String) -> Unit) {
        fun onClick(packageName: String) = onClickListener(packageName)
    }


    inner class RingToneViewHolder(val binding: ItemRingtoneSpinnerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RingTone) {

            binding.spinnerName.text = item.name

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RingToneViewHolder {

        val view = ItemRingtoneSpinnerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return RingToneViewHolder(view)


    }

    override fun onBindViewHolder(holder: RingToneViewHolder, position: Int) {

        val currentItem = getItem(position)
        holder.bind(currentItem)


    }
}

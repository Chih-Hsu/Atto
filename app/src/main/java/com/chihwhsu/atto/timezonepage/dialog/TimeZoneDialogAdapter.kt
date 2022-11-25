package com.chihwhsu.atto.timezonepage.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.data.AttoTimeZone
import com.chihwhsu.atto.databinding.ItemTimezoneBinding
import com.chihwhsu.atto.databinding.ItemTimezoneDialogBinding

class TimeZoneDialogAdapter(val onClickListener : TimeZoneClickListener) : ListAdapter<String, TimeZoneDialogAdapter.TimeZoneViewHolder>(object :
    DiffUtil.ItemCallback<String>(){

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}) {

    class TimeZoneClickListener(val onClickListener : (String)-> Unit){
        fun onClick(timeZone:String) = onClickListener(timeZone)
    }

    inner class TimeZoneViewHolder(val binding: ItemTimezoneDialogBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: String){
            binding.textTimezoneId.text = item.split("/").last()
            itemView.setOnClickListener {
                onClickListener.onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeZoneViewHolder {
        val view = ItemTimezoneDialogBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TimeZoneViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeZoneViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


}
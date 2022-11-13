package com.chihwhsu.atto.widgetpage


import android.appwidget.AppWidgetProviderInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.data.Widget
import com.chihwhsu.atto.databinding.ItemWidgetBinding

class WidgetAdapter(val onClickListener: WidgetOnClickListener) :
    ListAdapter<Widget, WidgetAdapter.WidgetViewHolder>(object :
        DiffUtil.ItemCallback<Widget>() {
        override fun areItemsTheSame(oldItem: Widget, newItem: Widget): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Widget, newItem: Widget): Boolean {
            return oldItem == newItem
        }
    }) {

    class WidgetOnClickListener(val onClickListener: (info:AppWidgetProviderInfo) -> Unit) {
        fun onClick(info:AppWidgetProviderInfo) = onClickListener(info)
    }

    inner class WidgetViewHolder(val binding: ItemWidgetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Widget) {
            if (item.previewImage != null){
                binding.imageWidgetIcon.setImageDrawable(item.previewImage)
            } else {
            binding.imageWidgetIcon.setImageDrawable(item.icon)
            }
            binding.textWidget.text = item.name
            itemView.setOnClickListener {
                onClickListener.onClick(item.info)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetViewHolder {
        val view = ItemWidgetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return WidgetViewHolder(view)
    }


    override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


}

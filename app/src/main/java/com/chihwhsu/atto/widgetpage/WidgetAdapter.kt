package com.chihwhsu.atto.widgetpage

import android.appwidget.AppWidgetProviderInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.databinding.ItemWidgetBinding

class WidgetAdapter(val onClickListener: WidgetOnClickListener) :
    ListAdapter<AppWidgetProviderInfo, WidgetAdapter.WidgetViewHolder>(object :
        DiffUtil.ItemCallback<AppWidgetProviderInfo>() {
        override fun areItemsTheSame(
            oldItem: AppWidgetProviderInfo,
            newItem: AppWidgetProviderInfo
        ): Boolean {
            return oldItem.label == newItem.label
        }

        override fun areContentsTheSame(
            oldItem: AppWidgetProviderInfo,
            newItem: AppWidgetProviderInfo
        ): Boolean {
            return oldItem.describeContents() == newItem.describeContents()
        }
    }) {

    class WidgetOnClickListener(val onClickListener: (label: String) -> Unit) {
        fun onClick(label: String) = onClickListener(label)
    }

    inner class WidgetViewHolder(val binding: ItemWidgetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AppWidgetProviderInfo) {
            binding.imageWidgetIcon.setImageDrawable(
                item.loadPreviewImage(
                    itemView.context,
                    itemView.resources.displayMetrics.density.toInt()
                )
            )

            binding.textWidget.text = item.label
            itemView.setOnClickListener {
                onClickListener.onClick(item.label)
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

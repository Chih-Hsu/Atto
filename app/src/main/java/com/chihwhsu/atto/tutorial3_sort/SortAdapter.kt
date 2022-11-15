package com.chihwhsu.atto.tutorial3_sort

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.data.AppListItem
import com.chihwhsu.atto.databinding.ItemAppListBinding
import com.chihwhsu.atto.databinding.ItemLabelSettingBinding
import com.chihwhsu.atto.ext.createGrayscale


class SortAdapter(
    val deleteOnClickListener: DeleteOnClickListener,
    val editOnClickListener: EditOnClickListener
) : ListAdapter<AppListItem, RecyclerView.ViewHolder>(object :
    DiffUtil.ItemCallback<AppListItem>() {
    override fun areItemsTheSame(oldItem: AppListItem, newItem: AppListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AppListItem, newItem: AppListItem): Boolean {
        return oldItem == newItem
    }
}) {

    companion object {
        const val APP_ITEM_VIEW_TYPE_LABEL = 0x00
        const val APP_ITEM_VIEW_TYPE_APP = 0x01
    }


    class DeleteOnClickListener(val onClickListener: (label: String) -> Unit) {
        fun onClick(label: String) = onClickListener(label)
    }

    class EditOnClickListener(val onClickListener: (label: String) -> Unit) {
        fun onClick(label: String) = onClickListener(label)
    }

    inner class LabelViewHolder(val binding: ItemLabelSettingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AppListItem.LabelItem) {
            binding.textLabel.text = item.title
            binding.textDelete.setOnClickListener {
                deleteOnClickListener.onClick(item.title)
            }
            binding.textEdit.setOnClickListener {
                editOnClickListener.onClick(item.title)
            }
        }
    }

    class AppViewHolder(val binding: ItemAppListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AppListItem.AppItem) {
//            binding.iconImage.setImageDrawable(item.icon)
//            item.app.icon?.let {
//                binding.iconImage.setImageBitmap(it.createGrayscale())
//            }
            binding.appName.text = item.app.appLabel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            APP_ITEM_VIEW_TYPE_LABEL -> {
                LabelViewHolder(
                    ItemLabelSettingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )
            }

            APP_ITEM_VIEW_TYPE_APP -> {
                val view = ItemAppListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
                return AppViewHolder(view)
            }
            else -> throw ClassCastException("Unknown viewType $viewType")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is LabelViewHolder -> {
                holder.bind((getItem(position) as AppListItem.LabelItem))
            }

            is AppViewHolder -> {
                holder.bind((getItem(position) as AppListItem.AppItem))
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AppListItem.LabelItem -> APP_ITEM_VIEW_TYPE_LABEL
            is AppListItem.AppItem -> APP_ITEM_VIEW_TYPE_APP
        }
    }

}
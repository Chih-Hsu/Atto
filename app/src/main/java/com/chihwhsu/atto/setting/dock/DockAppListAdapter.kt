package com.chihwhsu.atto.setting.dock

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.databinding.ItemDockBinding

class DockAppListAdapter : ListAdapter<App, DockAppListAdapter.AppViewHolder>(object :
        DiffUtil.ItemCallback<App>() {
        override fun areItemsTheSame(oldItem: App, newItem: App): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: App, newItem: App): Boolean {
            return oldItem == newItem
        }
    }) {

    inner class AppViewHolder(val binding: ItemDockBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: App) {

            setIcon(item)
        }

        private fun setIcon(item: App) {
            Glide.with(itemView.context)
                .load(item.iconPath)
                .into(binding.dockIconImage)

            val colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(0f)
            val filter = ColorMatrixColorFilter(colorMatrix)

            binding.dockIconImage.colorFilter = filter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = ItemDockBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}

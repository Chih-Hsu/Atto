package com.chihwhsu.atto.tutorial.dock

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.databinding.ItemAppListBinding

class AppListAdapter(val viewModel: DockViewModel, val onClickListener: AppOnClickListener) :
    ListAdapter<App, AppListAdapter.AppViewHolder>(object :
        DiffUtil.ItemCallback<App>() {
        override fun areItemsTheSame(oldItem: App, newItem: App): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: App, newItem: App): Boolean {
            return oldItem == newItem
        }
    }) {

    class AppOnClickListener(val onClickListener: (appLabel: String) -> Unit) {
        fun onClick(appLabel: String) = onClickListener(appLabel)

    }

    inner class AppViewHolder(val binding: ItemAppListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: App) {

            checkItemInDock(item)

            Glide.with(itemView.context)
                .load(item.iconPath)
                .into(binding.iconImage)

            val colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(0f)
            val filter = ColorMatrixColorFilter(colorMatrix)

            binding.iconImage.colorFilter = filter

            binding.appName.text = item.appLabel

            itemView.setOnClickListener {
                onClickListener.onClick(item.appLabel)
                checkItemInDock(item)
            }
        }

        private fun checkItemInDock(item: App) {
            viewModel.dockAppList.value?.let { list ->
                if (!list.none { it.appLabel == item.appLabel }) {
                    binding.iconBackground.setBackgroundResource(R.drawable.icon_background_selected)
                } else {
                    binding.iconBackground.setBackgroundResource(R.drawable.icon_background)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = ItemAppListBinding.inflate(
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
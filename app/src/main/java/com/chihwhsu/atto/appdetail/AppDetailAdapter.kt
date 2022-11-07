package com.chihwhsu.atto.appdetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.Theme
import com.chihwhsu.atto.databinding.ItemAppListBinding
import com.chihwhsu.atto.databinding.ItemBackgroundListBinding

class AppDetailAdapter :
    ListAdapter<Theme, AppDetailAdapter.ThemeViewHolder>(object : DiffUtil.ItemCallback<Theme>() {

        override fun areContentsTheSame(oldItem: Theme, newItem: Theme): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Theme, newItem: Theme): Boolean {
            return oldItem.index == newItem.index
        }

    }) {

    class ThemeViewHolder(val binding: ItemBackgroundListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Theme) {
            when (item) {
                Theme.DEFAULT -> {}
                Theme.BLACK -> {
                    binding.iconBackground.setCardBackgroundColor(ResourcesCompat.getColor(itemView.resources,R.color.dark_grey,null))
                }
                Theme.HIGH_LIGHT -> {
                    binding.iconBackground.setCardBackgroundColor(ResourcesCompat.getColor(itemView.resources,R.color.yellow,null))
                }
                Theme.KANAHEI -> { binding.kanaImage.visibility = View.VISIBLE }
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val view = ItemBackgroundListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThemeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}
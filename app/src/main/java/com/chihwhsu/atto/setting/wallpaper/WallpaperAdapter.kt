package com.chihwhsu.atto.setting.wallpaper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.data.Wallpaper
import com.chihwhsu.atto.databinding.ItemWallpaperBinding

class WallpaperAdapter : ListAdapter<Wallpaper, WallpaperAdapter.WallpaperViewHolder>(object :
        DiffUtil.ItemCallback<Wallpaper>() {
        override fun areContentsTheSame(oldItem: Wallpaper, newItem: Wallpaper): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Wallpaper, newItem: Wallpaper): Boolean {
            return oldItem.id == newItem.id
        }
    }) {

    class WallpaperViewHolder(val binding: ItemWallpaperBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Wallpaper) {
            binding.wallpaperContainer.setImageDrawable(item.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val view = ItemWallpaperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WallpaperViewHolder(view)
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}

package com.chihwhsu.atto.main


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.databinding.ItemAppListBinding
import com.chihwhsu.atto.databinding.ItemDockBinding
import com.chihwhsu.atto.ext.createGrayscale


class DockAdapter (val onClickListener : DockOnClickListener) : ListAdapter<App, DockAdapter.AppViewHolder>(object :
    DiffUtil.ItemCallback<App>(){
    override fun areItemsTheSame(oldItem: App, newItem: App): Boolean {
        return oldItem.packageName == newItem.packageName
    }

    override fun areContentsTheSame(oldItem: App, newItem: App): Boolean {
        return oldItem == newItem
    }
}) {

    class DockOnClickListener(val onClickListener:(packageName:String)->Unit){
        fun onClick(packageName: String)=onClickListener(packageName)

    }

    inner class AppViewHolder(val binding: ItemDockBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: App){

            item.icon?.let {
                binding.dockIconImage.setImageBitmap(it.createGrayscale())
            }
            itemView.setOnClickListener {
                onClickListener.onClick(item.packageName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = ItemDockBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

}
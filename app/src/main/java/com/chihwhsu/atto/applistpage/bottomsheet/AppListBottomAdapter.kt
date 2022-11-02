package com.chihwhsu.atto.applistpage.bottomsheet

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.R
import com.chihwhsu.atto.applistpage.AppListViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.AppListItem
import com.chihwhsu.atto.databinding.ItemAppListBinding
import com.chihwhsu.atto.databinding.ItemLabelBinding
import com.chihwhsu.atto.ext.createGrayscale


class AppListBottomAdapter  (val appOnClickListener : AppOnClickListener) : ListAdapter<AppListItem, RecyclerView.ViewHolder>(object :
    DiffUtil.ItemCallback<AppListItem>(){
    override fun areItemsTheSame(oldItem: AppListItem, newItem: AppListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AppListItem, newItem: AppListItem): Boolean {
        return oldItem == newItem
    }
}) {

    companion object{
        const val APP_ITEM_VIEW_TYPE_LABEL = 0x00
        const val APP_ITEM_VIEW_TYPE_APP = 0x01
    }



    class AppOnClickListener(val onClickListener:(packageName:String)->Unit){
        fun onClick(packageName: String) = onClickListener(packageName)
    }


    inner class LabelViewHolder(val binding: ItemLabelBinding): RecyclerView.ViewHolder(binding.root){

        fun bind( item : AppListItem.LabelItem){
            binding.textLabel.apply {
                text = item.title
                setTextColor(ResourcesCompat.getColor(itemView.resources, R.color.light_grey,null))
            }

        }
    }

    inner class AppViewHolder(val binding: ItemAppListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: AppListItem.AppItem){


            item.app.icon?.let {
                binding.iconImage.setImageBitmap(it.createGrayscale())
            }

            binding.appName.text = item.app.appLabel

            itemView.setOnClickListener {
                appOnClickListener.onClick(item.app.packageName)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            APP_ITEM_VIEW_TYPE_LABEL -> {
                LabelViewHolder(
                    ItemLabelBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,false))
            }

            APP_ITEM_VIEW_TYPE_APP -> {
                val view = ItemAppListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,false)
                return AppViewHolder(view)
            }
            else -> throw ClassCastException("Unknown viewType $viewType")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder){
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
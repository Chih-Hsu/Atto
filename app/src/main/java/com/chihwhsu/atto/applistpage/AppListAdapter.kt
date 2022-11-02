package com.chihwhsu.atto.applistpage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.AppListItem
import com.chihwhsu.atto.databinding.ItemAppListBinding
import com.chihwhsu.atto.databinding.ItemLabelBinding
import com.chihwhsu.atto.ext.createGrayscale

class AppListAdapter  (val appOnClickListener : AppOnClickListener,val viewModel: AppListViewModel) : ListAdapter<AppListItem, RecyclerView.ViewHolder>(object :
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
                setTextColor(ResourcesCompat.getColor(itemView.resources,R.color.light_grey,null))
            }


            itemView.setOnClickListener {
//                labelOnClickListener.onClick(item.title)
                if (viewModel.isHide.containsKey(item.title)){
                    val value = viewModel.isHide.get(item.title) as Boolean
                    viewModel.isHide.put(item.title,!value)
                    Log.d("select","update ${viewModel.isHide}")

                }else{
                    viewModel.isHide.put(item.title,true)
                    Log.d("select","first ${viewModel.isHide.get("TRY")}")

                }
                notifyDataSetChanged()
            }
        }
    }

    inner class AppViewHolder(val binding: ItemAppListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: AppListItem.AppItem){

            showAppOrNot(item.app)

            item.app.icon?.let {
                binding.iconImage.setImageBitmap(it.createGrayscale())
            }
            itemView.setOnClickListener {
                appOnClickListener.onClick(item.app.packageName)
            }

        }

        fun showAppOrNot(app : App){
            if (viewModel.isHide.containsKey(app.label) && viewModel.isHide.get(app.label) == true){
                // Show ItemView
                itemView.visibility = View.VISIBLE
                itemView.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            }else{
                // Hide ItemView and set width/height to 0
                itemView.visibility = View.GONE
                itemView.layoutParams = RecyclerView.LayoutParams(0,0)
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
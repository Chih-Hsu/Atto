package com.chihwhsu.atto.setting.sort.addlabel

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

class AddLabelListAdapter (val viewModel: AddLabelViewModel, val onClickListener: AppOnClickListener) : ListAdapter<App, AddLabelListAdapter.AppViewHolder>(object :
    DiffUtil.ItemCallback<App>(){
    override fun areItemsTheSame(oldItem: App, newItem: App): Boolean {
        return oldItem.packageName == newItem.packageName
    }

    override fun areContentsTheSame(oldItem: App, newItem: App): Boolean {
        return oldItem == newItem
    }
}) {

    class AppOnClickListener(val onClickListener:(app:App)->Unit){
        fun onClick(app: App) = onClickListener(app)

    }

    inner class AppViewHolder(val binding:ItemAppListBinding): RecyclerView.ViewHolder(binding.root){


        fun bind(item:App){

            checkItemInRemain(item)

//            item.icon?.let {
//                binding.iconImage.setImageBitmap(it.createGrayscale())
//            }
            Glide.with(itemView.context)
                .load(item.iconPath)
                .into(binding.iconImage)

            val colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(0f)
            val filter = ColorMatrixColorFilter(colorMatrix)

            binding.iconImage.colorFilter = filter

            binding.appName.text = item.appLabel

            // Default background
//            binding.iconBackground.setBackgroundResource(R.drawable.icon_background)

            itemView.setOnClickListener {
                onClickListener.onClick(item)
                checkItemInRemain(item)
            }
        }

        private fun checkItemInRemain(item:App){

                if (!viewModel.remainList.filter { it.appLabel == item.appLabel }.isEmpty()){
                    binding.iconBackground.setBackgroundResource(R.drawable.icon_background_selected)
                }else{
                    binding.iconBackground.setBackgroundResource(R.drawable.icon_background)
                }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = ItemAppListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

}
package com.chihwhsu.atto.applistpage.bottomsheet

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.AppListItem
import com.chihwhsu.atto.data.Theme
import com.chihwhsu.atto.databinding.ItemAppListBinding
import com.chihwhsu.atto.databinding.ItemLabelBinding


class AppListBottomAdapter(
    val appOnClickListener: AppOnClickListener,
    val longClickListener: LongClickListener
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

    class AppOnClickListener(val onClickListener: (app: App) -> Unit) {
        fun onClick(app: App) = onClickListener(app)
    }

    class LongClickListener(val onClickListener: (app: App) -> Unit) {
        fun onClick(app: App) = onClickListener(app)
    }


    inner class LabelViewHolder(val binding: ItemLabelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AppListItem.LabelItem) {
            binding.textLabel.apply {
                text = item.title
                setTextColor(ResourcesCompat.getColor(itemView.resources, R.color.light_grey, null))
            }
        }
    }

    inner class AppViewHolder(val binding: ItemAppListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AppListItem.AppItem) {

            binding.appName.text = item.app.appLabel

            setAlphaByInstallState(item)  // App is installed or not
            setIcon(item)
            setBackground(item)   // set background color according to app theme
            setAppClickableStateByEnable(item)  // App is locked or not


            itemView.setOnLongClickListener {
                longClickListener.onClick(item.app)
                true
            }
        }

        private fun setAlphaByInstallState(item: AppListItem.AppItem) {
            if (item.app.installed) {

                itemView.alpha = 1F
            } else {
                // if app is not installed , show half transparency
                itemView.alpha = 0.3F
            }
        }

        private fun setAppClickableStateByEnable(item: AppListItem.AppItem) {
            if (item.app.isEnable) {

                itemView.setOnClickListener {
                    appOnClickListener.onClick(item.app)
                }

                binding.iconBackground.foreground = null
                binding.lockImage.visibility = View.GONE

            } else {

                binding.iconBackground.foreground = ResourcesCompat.getDrawable(
                    itemView.resources,
                    R.drawable.icon_background_lock,
                    null
                )
                binding.lockImage.visibility = View.VISIBLE

            }
        }

        private fun setBackground(item: AppListItem.AppItem) {
            when (item.app.theme) {
                Theme.DEFAULT.index -> {
                    binding.iconBackground.setBackgroundResource(R.drawable.icon_background)
                    binding.kanaImage.visibility = View.INVISIBLE
                }
                Theme.BLACK.index -> {
                    binding.iconBackground.setBackgroundResource(R.drawable.icon_background_black)
                    binding.kanaImage.visibility = View.INVISIBLE
                }
                Theme.HIGH_LIGHT.index -> {
                    binding.iconBackground.setBackgroundResource(R.drawable.icon_backgroung_hightlight)
                    binding.kanaImage.visibility = View.INVISIBLE
                }
                Theme.KANAHEI.index -> {
                    binding.kanaImage.visibility = View.VISIBLE
                }
                else -> {
                    binding.iconBackground.setBackgroundResource(R.drawable.icon_background)
                    binding.kanaImage.visibility = View.INVISIBLE
                }
            }
        }

        private fun setIcon(item: AppListItem.AppItem) {
            Glide.with(itemView.context)
                .load(item.app.iconPath)
                .into(binding.iconImage)

            val colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(0f)
            val filter = ColorMatrixColorFilter(colorMatrix)

            binding.iconImage.colorFilter = filter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            APP_ITEM_VIEW_TYPE_LABEL -> {
                LabelViewHolder(
                    ItemLabelBinding.inflate(
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
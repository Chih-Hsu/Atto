package com.chihwhsu.atto.applistpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.data.AppListItem
import com.chihwhsu.atto.databinding.FragmentAppListBinding
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.tutorial3_sort.SortAdapter
import java.util.*
import kotlin.collections.ArrayList

class AppListFragment : Fragment() {

    private val viewModel by viewModels<AppListViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAppListBinding.inflate(inflater,container,false)

        val adapter = AppListAdapter(
            AppListAdapter.AppOnClickListener {
                val launchAppIntent = requireContext().packageManager.getLaunchIntentForPackage(it)
                startActivity(launchAppIntent)
        },AppListAdapter.LongClickListener { app ->
//          findNavController().navigate(NavigationDirections.actionGlobalAppInfoDialog(app))
            }
        ,viewModel)
        binding.appRecyclerView.adapter = adapter

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,0){

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
//                if (viewHolder.getItemViewType() != target.getItemViewType()) {
//                    return true
//                }
//                if (viewHolder.itemViewType != target.itemViewType){
//                    val fromItem = adapter.currentList.get(viewHolder.adapterPosition)
//                    val toItem = adapter.currentList.get(target.adapterPosition)
//                    adapter.currentList.remove(adapter.currentList.get(viewHolder.adapterPosition))
//                    adapter.currentList.add(viewHolder.adapterPosition,toItem)
//                    adapter.currentList.remove(adapter.currentList.get(target.adapterPosition))
//                    adapter.currentList.add(target.adapterPosition,fromItem)
//                }
//                val fromPosition = viewHolder.adapterPosition
//                val toPosition = target.adapterPosition
//                val arrayList = java.util.ArrayList(adapter.currentList)
////                Collections.swap(adapter.currentList,fromPosition,toPosition)
//
//
//                adapter.notifyItemMoved(fromPosition,toPosition)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }
        }

        val itemHelper = ItemTouchHelper(simpleCallback)
        itemHelper.attachToRecyclerView(binding.appRecyclerView)

        val layoutManager = binding.appRecyclerView.layoutManager as GridLayoutManager

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return when(adapter.getItemViewType(position)){
                    SortAdapter.APP_ITEM_VIEW_TYPE_LABEL -> 5
                    SortAdapter.APP_ITEM_VIEW_TYPE_APP -> 1
                    else -> 1
                }
            }
        }

        viewModel.appList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(viewModel.resetList(it,requireContext()))
        })

        return binding.root
    }
}
package com.chihwhsu.atto.tutorial3_sort

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.chihwhsu.atto.data.database.AttoDatabase
import com.chihwhsu.atto.databinding.FragmentAppSorttingBinding
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.factory.SortViewModelFactory
import com.chihwhsu.atto.tutorial1_wallpaper.WallpaperViewModel
import com.chihwhsu.atto.tutorial2_dock.AppListAdapter
import com.chihwhsu.atto.tutorial3_sort.addlabel.AddLabelListAdapter

class SortFragment : Fragment() {

    private val viewModel by viewModels<SortViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAppSorttingBinding.inflate(inflater,container,false)

        binding.addButton.setOnClickListener {
            findNavController().navigate(SortFragmentDirections.actionSortFragmentToAddLabelFragment())
        }

        val adapter = SortAdapter()

        binding.sortRecyclerView.adapter = adapter
        val layoutManager = binding.sortRecyclerView.layoutManager as GridLayoutManager

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
            adapter.submitList(viewModel.resetList(it))
//            Log.d("select","$it")
        })

        binding.buttonPrevious.setOnClickListener {
            findNavController().navigate(SortFragmentDirections.actionSortFragmentToDockSelectFragment())
        }

        binding.buttonNext.setOnClickListener {
            findNavController().navigate(SortFragmentDirections.actionSortFragmentToGetUsageFragment())
        }
















        return binding.root
    }
}
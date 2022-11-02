package com.chihwhsu.atto.applistpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.chihwhsu.atto.databinding.FragmentAppListBinding
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.tutorial3_sort.SortAdapter

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
        },
        viewModel)
        binding.appRecyclerView.adapter = adapter

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
            adapter.submitList(viewModel.resetList(it))
        })





        return binding.root
    }
}
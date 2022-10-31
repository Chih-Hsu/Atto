package com.chihwhsu.atto.tutorial2_dock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.databinding.FragmentDockSelectBinding
import com.chihwhsu.atto.factory.DockSelectViewModelFactory
import androidx.appcompat.widget.SearchView.OnQueryTextListener

class DockSelectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDockSelectBinding.inflate(inflater,container,false)

        // viewModel setting
        val viewModelFactory = DockSelectViewModelFactory(requireActivity().applicationContext)
        val viewModel = ViewModelProvider(this,viewModelFactory).get(DockSelectViewModel::class.java)

        // recyclerview setting
        val appListAdapter = AppListAdapter(viewModel,AppListAdapter.AppOnClickListener { appLabel ->
            viewModel.selectApp(appLabel)
        })
        binding.appListRecyclerview.adapter = appListAdapter

        viewModel.appList.observe(viewLifecycleOwner, Observer {
            appListAdapter.submitList(it)
        })

        val dockListAdapter = DockAppListAdapter()
        binding.dockRecyclerview.adapter = dockListAdapter
        viewModel.dockAppList.observe(viewLifecycleOwner, Observer {
            dockListAdapter.submitList(it)
        })


        // searchView
        binding.appSearchView.setOnQueryTextListener(object : OnQueryTextListener{
                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.filterList(newText)
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
        })

        return binding.root
    }


}
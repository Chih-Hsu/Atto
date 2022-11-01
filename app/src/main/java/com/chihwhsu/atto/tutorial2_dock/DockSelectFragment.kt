package com.chihwhsu.atto.tutorial2_dock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.chihwhsu.atto.databinding.FragmentDockSelectBinding
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.SettingActivity
import com.chihwhsu.atto.applistpage.AppListViewModel
import com.chihwhsu.atto.data.database.AttoDatabase
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.factory.DockViewModelFactory

class DockSelectFragment : Fragment() {

    private val viewModel by viewModels<DockViewModel> { getVmFactory(requireActivity().packageManager) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDockSelectBinding.inflate(inflater,container,false)


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

        binding.buttonNext.setOnClickListener {
            findNavController().navigate(DockSelectFragmentDirections.actionDockSelectFragmentToSortFragment())
        }

        binding.buttonPrevious.setOnClickListener {
            findNavController().navigate(DockSelectFragmentDirections.actionDockSelectFragmentToWallpaperFragment())
        }

        return binding.root
    }


}
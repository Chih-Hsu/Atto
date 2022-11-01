package com.chihwhsu.atto.tutorial2_dock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.chihwhsu.atto.databinding.FragmentDockSelectBinding
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.SettingActivity

class DockSelectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDockSelectBinding.inflate(inflater,container,false)

        val settingViewModel = (requireActivity() as SettingActivity).viewModel

        // recyclerview setting
        val appListAdapter = AppListAdapter(settingViewModel,AppListAdapter.AppOnClickListener { appLabel ->
            settingViewModel.selectApp(appLabel)
        })
        binding.appListRecyclerview.adapter = appListAdapter

        settingViewModel.appList.observe(viewLifecycleOwner, Observer {
            appListAdapter.submitList(it)
        })

        val dockListAdapter = DockAppListAdapter()
        binding.dockRecyclerview.adapter = dockListAdapter
        settingViewModel.dockAppList.observe(viewLifecycleOwner, Observer {
            dockListAdapter.submitList(it)
        })


        // searchView
        binding.appSearchView.setOnQueryTextListener(object : OnQueryTextListener{
                override fun onQueryTextChange(newText: String?): Boolean {
                    settingViewModel.filterList(newText)
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
package com.chihwhsu.atto.tutorial.dock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.databinding.FragmentDockSelectBinding
import com.chihwhsu.atto.ext.getVmFactory

class DockSelectFragment : Fragment() {

    private val viewModel by viewModels<DockViewModel> { getVmFactory(requireActivity().packageManager) }
    private lateinit var binding: FragmentDockSelectBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDockSelectBinding.inflate(inflater, container, false)

        setSearchView()

        // set app list recyclerview
        val appListAdapter =
            AppListAdapter(viewModel, AppListAdapter.AppOnClickListener { appLabel ->
                viewModel.selectApp(appLabel)
            })
        binding.appListRecyclerview.adapter = appListAdapter

        viewModel.dataList.observe(viewLifecycleOwner) {
            viewModel.setAppList(it)
        }

        viewModel.appList.observe(viewLifecycleOwner) {
            appListAdapter.submitList(it)
        }


        // set dock list recyclerview
        val dockListAdapter = DockAppListAdapter()

        binding.dockRecyclerview.adapter = dockListAdapter

        viewModel.dockList.observe(viewLifecycleOwner) {
            viewModel.setDockList(it)
        }

        viewModel.dockAppList.observe(viewLifecycleOwner) {
            dockListAdapter.submitList(it)
        }


        // set button navigation
        binding.buttonNext.setOnClickListener {
            findNavController()
                .navigate(DockSelectFragmentDirections.actionDockSelectFragmentToSortFragment())
        }

        binding.buttonPrevious.setOnClickListener {
            findNavController()
                .navigate(DockSelectFragmentDirections.actionDockSelectFragmentToWallpaperFragment())
        }

        return binding.root
    }

    private fun setSearchView() {
        binding.appSearchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterList(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
    }


}
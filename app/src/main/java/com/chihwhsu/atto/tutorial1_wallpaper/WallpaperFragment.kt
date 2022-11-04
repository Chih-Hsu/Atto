package com.chihwhsu.atto.tutorial1_wallpaper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.chihwhsu.atto.databinding.FragmentWallpaperBinding
import com.chihwhsu.atto.ext.getVmFactory

class WallpaperFragment : Fragment() {

    private val viewModel by viewModels<WallpaperViewModel> { getVmFactory(resources) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWallpaperBinding.inflate(inflater,container,false)

        val adapter = WallpaperAdapter()
        binding.wallpaperRecyclerview.adapter = adapter

        // LinearSnapHelper
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.wallpaperRecyclerview)

        viewModel.wallpapers.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.checkButton.setOnClickListener {

            val manager = binding.wallpaperRecyclerview.layoutManager as LinearLayoutManager
            val currentPosition = manager.findFirstVisibleItemPosition()
            val drawable = adapter.currentList.get(currentPosition).image
            viewModel.setWallPaper(requireContext(),drawable)
//            viewModel.navigateToNext()
        }

        viewModel.navigationToNext.observe(viewLifecycleOwner, Observer { canNavigate ->
            if (canNavigate){
            findNavController().navigate(WallpaperFragmentDirections.actionWallpaperFragmentToDockSelectFragment())
            viewModel.doneNavigateToNext()}
        })





        return binding.root
    }
}
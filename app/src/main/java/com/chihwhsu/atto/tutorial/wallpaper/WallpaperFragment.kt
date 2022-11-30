package com.chihwhsu.atto.tutorial.wallpaper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.chihwhsu.atto.component.CenterZoomLayoutManager
import com.chihwhsu.atto.databinding.FragmentWallpaperBinding
import com.chihwhsu.atto.ext.getVmFactory

class WallpaperFragment : Fragment() {

    private val viewModel by viewModels<WallpaperViewModel> { getVmFactory(resources) }
    private lateinit var binding: FragmentWallpaperBinding
    private lateinit var adapter: WallpaperAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWallpaperBinding.inflate(inflater, container, false)

        binding.checkButton.setOnClickListener {
            setWallPaper(adapter)
        }

        binding.textKeepWallpaper.setOnClickListener {
            findNavController().navigate(WallpaperFragmentDirections.actionWallpaperFragmentToDockSelectFragment())
        }

        setRecyclerview()

        viewModel.navigationToNext.observe(viewLifecycleOwner) { canNavigate ->
            if (canNavigate) {
                findNavController().navigate(WallpaperFragmentDirections.actionWallpaperFragmentToDockSelectFragment())
                viewModel.doneNavigateToNext()
            }
        }

        return binding.root
    }

    private fun setWallPaper(adapter: WallpaperAdapter) {
        val manager = binding.wallpaperRecyclerview.layoutManager as LinearLayoutManager
        val currentPosition = manager.findFirstVisibleItemPosition()
        val drawable = adapter.currentList[currentPosition].image
        viewModel.setWallPaper(requireContext(), drawable)
    }

    private fun setRecyclerview() {

        adapter = WallpaperAdapter()
        binding.wallpaperRecyclerview.adapter = adapter

        // LinearSnapHelper
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.wallpaperRecyclerview)

        val layoutManager =
            CenterZoomLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.wallpaperRecyclerview.layoutManager = layoutManager
        binding.wallpaperRecyclerview.scrollToPosition(1)

        viewModel.wallpapers.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }


    }
}
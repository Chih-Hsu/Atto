package com.chihwhsu.atto.tutorial_wallpaper

import android.app.WallpaperManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.chihwhsu.atto.R
import com.chihwhsu.atto.databinding.FragmentWallpaperBinding
import com.chihwhsu.atto.factory.WallpaperViewModelFactory

class WallpaperFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWallpaperBinding.inflate(inflater,container,false)
        val viewModelFactory = WallpaperViewModelFactory(resources)
        val viewModel = ViewModelProvider(this,viewModelFactory).get(WallpaperViewModel::class.java)

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
            val bitmap = drawable.toBitmap(drawable.minimumWidth,drawable.minimumHeight)
            WallpaperManager.getInstance(requireContext()).setBitmap(bitmap)
        }



        return binding.root
    }
}
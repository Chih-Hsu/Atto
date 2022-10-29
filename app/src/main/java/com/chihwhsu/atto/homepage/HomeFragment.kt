package com.chihwhsu.atto.homepage

import android.app.WallpaperManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater,container,false)

//        binding.textView.setOnClickListener {
//            val bitMap = BitmapFactory.decodeResource(resources,R.drawable.wallpaper_plant)
//            WallpaperManager.getInstance(requireContext()).setBitmap(bitMap)
//        }
        val list = mutableListOf<Event>(Event(1,true),Event(2,true)
            ,Event(1,false),Event(1,false)
            ,Event(1,false),Event(1,false)
            ,Event(1,false),Event(1,false)
            ,Event(1,false),Event(1,false))

        val adapter = EventAdapter()
        binding.eventList.adapter = adapter

        adapter.submitList(list)
        return binding.root
    }
}
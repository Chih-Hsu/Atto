package com.chihwhsu.atto.applistpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chihwhsu.atto.databinding.FragmentAppListContainerBinding

class AppListContainerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentAppListContainerBinding.inflate(inflater, container, false)

        // set ViewPager2
        val adapter = AppListPagerAdapter(this)
        binding.viewPager.adapter = adapter

//        binding.viewPager.registerOnPageChangeCallback(object :)

        return binding.root
    }
}
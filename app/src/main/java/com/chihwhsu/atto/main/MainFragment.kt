package com.chihwhsu.atto.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chihwhsu.atto.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater,container,false)

        // set ViewPager2
        val adapter = MainViewPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.post {
            binding.viewPager.setCurrentItem(1, true)
        }


        return binding.root
    }
}
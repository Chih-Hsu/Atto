package com.chihwhsu.atto.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chihwhsu.atto.home.HomeFragment

class MainViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        return when (position) {

            0 -> HomeFragment()
            1 -> HomeFragment()
            2 -> HomeFragment()
            else -> throw IllegalArgumentException("Unknown Fragment")
        }
    }
}
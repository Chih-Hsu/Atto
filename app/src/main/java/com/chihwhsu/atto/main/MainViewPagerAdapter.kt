package com.chihwhsu.atto.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chihwhsu.atto.applistpage.AppListContainerFragment
import com.chihwhsu.atto.applistpage.AppListFragment
import com.chihwhsu.atto.homepage.HomeFragment
import com.chihwhsu.atto.widgetpage.WidgetFragment

class MainViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        return when (position) {

            0 -> HomeFragment()
            1 -> AppListContainerFragment()
            2 -> WidgetFragment()
            else -> throw IllegalArgumentException("Unknown Fragment")
        }
    }
}
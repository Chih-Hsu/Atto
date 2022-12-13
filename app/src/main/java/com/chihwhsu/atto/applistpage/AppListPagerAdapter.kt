package com.chihwhsu.atto.applistpage

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chihwhsu.atto.applistpage.bottomsheet.AppListBottomFragment

class AppListPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        return when (position) {

            0 -> AppListFragment()
            1 -> AppListBottomFragment()
            else -> throw IllegalArgumentException("Unknown Fragment")
        }
    }
}

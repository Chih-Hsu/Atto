package com.chihwhsu.atto.clock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chihwhsu.atto.databinding.FragmentClockBinding
import com.google.android.material.tabs.TabLayoutMediator



class ClockFragment : Fragment() {

    companion object{
        const val ALARM_TYPE = 1
        const val TODO_TYPE = 2
        const val POMODORO_TYPE = 3
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentClockBinding.inflate(inflater,container,false)

        val adapter = ClockViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        // set TabLayout
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Alarm"
                }
                1 -> tab.text = "Todo"
                2 -> tab.text = "Pomodoro"
            }
        }.attach()



        return binding.root
    }

}
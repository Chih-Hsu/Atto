package com.chihwhsu.atto.clock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chihwhsu.atto.databinding.FragmentClockBinding
import com.google.android.material.tabs.TabLayoutMediator

class ClockFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentClockBinding.inflate(inflater, container, false)

        val adapter = ClockViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        // set TabLayout
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = ALARM
                1 -> tab.text = TODO
                2 -> tab.text = POMODORO
            }
        }.attach()

        return binding.root
    }

    companion object {
        private const val ALARM = "Alarm"
        private const val TODO = "Todo"
        private const val POMODORO = "Pomodoro"
    }
}

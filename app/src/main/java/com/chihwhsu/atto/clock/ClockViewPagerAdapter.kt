package com.chihwhsu.atto.clock

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.chihwhsu.atto.clock.alarm.AlarmListFragment
import com.chihwhsu.atto.clock.pomodoro.PomodoroFragment
import com.chihwhsu.atto.clock.todo.TodoFragment

class ClockViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> AlarmListFragment()
            1 -> TodoFragment()
            2 -> PomodoroFragment()
            else -> throw IllegalArgumentException("Unknown Fragment")
        }
    }
}

package com.chihwhsu.atto.clock.pomodoro

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chihwhsu.atto.R
import com.chihwhsu.atto.databinding.FragmentPomodoroBinding
import com.google.android.material.datepicker.MaterialDatePicker

class PomodoroFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPomodoroBinding.inflate(inflater,container,false)



        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .build()





//        Log.d("calendar", "${binding.datePicker.month}")
//
//        binding.datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
//            Log.d("calendar", "${dayOfMonth}")
//        }
//
//        binding.datePicker.setBackgroundResource(R.drawable.week_corner_background_select)
//        binding.datePicker


        return binding.root
    }
}
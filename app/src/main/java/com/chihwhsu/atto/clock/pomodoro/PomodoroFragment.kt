package com.chihwhsu.atto.clock.pomodoro

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chihwhsu.atto.R
import com.chihwhsu.atto.applistpage.AppListViewModel
import com.chihwhsu.atto.databinding.FragmentPomodoroBinding
import com.chihwhsu.atto.ext.formatHour
import com.chihwhsu.atto.ext.formatMinutes
import com.chihwhsu.atto.ext.getVmFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import java.text.SimpleDateFormat

class PomodoroFragment : Fragment() {

    private val viewModel by viewModels<PomodoroViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPomodoroBinding.inflate(inflater,container,false)

        // set current time
        val currentTime = System.currentTimeMillis() + 60000
        val simpleFormat = SimpleDateFormat("a hh:mm")
        binding.hourMinute.text = simpleFormat.format(currentTime)

        viewModel.labelList.observe(viewLifecycleOwner, Observer {
            val setList = it.toSet()
            val newList = mutableListOf<String>()
            newList.add("All Apps")
            for (item in setList){
                if (!item.isNullOrEmpty() && item != "dock"){
                    newList.add(item)
                }
            }
//            Log.d("pomodoro","$it new = $newList")
            binding.labelSpinner.adapter = ArrayAdapter<String>(requireContext(),
                R.layout.item_ringtone_spinner,newList)
        })


        val timePicker =
            MaterialTimePicker.Builder().build()


        binding.imageTimeEdit.setOnClickListener {
            timePicker.show(parentFragmentManager,"Time")
        }

        timePicker.addOnPositiveButtonClickListener {
            // replace textview text
            val amPm = if (timePicker.hour<=12)"AM" else "PM"
            binding.hourMinute.text = resources.getString(
                R.string.a_hh_mm,
                amPm,
                timePicker.hour.formatHour(),
                timePicker.minute.formatMinutes()
            )

//            binding.am.text = if (timePicker.hour <= 12) {
//                "am"
//            } else {
//                "pm"
//            }
        }







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
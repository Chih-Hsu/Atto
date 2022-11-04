package com.chihwhsu.atto.clock.todo

import android.graphics.RenderEffect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.chihwhsu.atto.R
import com.chihwhsu.atto.clock.ClockFragment
import com.chihwhsu.atto.databinding.FragmentTodoBinding
import com.chihwhsu.atto.ext.formatHour
import com.chihwhsu.atto.ext.formatMinutes
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import java.text.SimpleDateFormat
import java.util.*

class TodoFragment : Fragment() {

    private lateinit var binding: FragmentTodoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater,container,false)

        initDateAndTimePicker()





        return binding.root
    }

    private fun initDateAndTimePicker(){

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .build()

        val timePicker =
            MaterialTimePicker.Builder().build()

        binding.imageDayEdit.setOnClickListener {
            datePicker.show(parentFragmentManager,"Date")
        }

        binding.imageTimeEdit.setOnClickListener {
            timePicker.show(parentFragmentManager,"Time")
        }

        datePicker.addOnPositiveButtonClickListener {
            val dateFormat = SimpleDateFormat("MMMM dd")
            val dateString = dateFormat.format(it)
            Log.d("calendar","$it")
            binding.monthDays.text = dateString.uppercase()
        }

        timePicker.addOnPositiveButtonClickListener {
            // replace textview text
            binding.hourMinute.text = resources.getString(
                R.string.hh_mm,
                timePicker.hour.formatHour(),
                timePicker.minute.formatMinutes()
            )

            binding.am.text = if (timePicker.hour <= 12) {
                "am"
            } else {
                "pm"
            }
        }

    }


}
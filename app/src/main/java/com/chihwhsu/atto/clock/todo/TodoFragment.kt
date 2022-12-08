package com.chihwhsu.atto.clock.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.R
import com.chihwhsu.atto.databinding.FragmentTodoBinding
import com.chihwhsu.atto.ext.formatHour
import com.chihwhsu.atto.ext.formatMinutes
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.util.AM
import com.chihwhsu.atto.util.HOUR
import com.chihwhsu.atto.util.MINUTE
import com.chihwhsu.atto.util.PM
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import java.text.SimpleDateFormat
import java.util.*

class TodoFragment : Fragment() {

    private lateinit var binding: FragmentTodoBinding
    private val viewModel by viewModels<TodoViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoBinding.inflate(inflater, container, false)

        initDateAndTimePicker()

        // show current time + 10 minutes
        val currentTime = System.currentTimeMillis() + (10 * MINUTE)
        val simpleFormat = SimpleDateFormat("a  hh:mm", Locale.getDefault())
        binding.hourMinute.text = simpleFormat.format(currentTime)
        val monthFormat = SimpleDateFormat("MMMM dd", Locale.getDefault())
        binding.monthDays.text = monthFormat.format(currentTime).uppercase()

        binding.todoTitle.doAfterTextChanged {
            viewModel.setTitle(it.toString())
        }

        binding.todoContent.doAfterTextChanged {
            viewModel.setContent(it.toString())
        }

        binding.button.setOnClickListener {
            viewModel.saveEvent()
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun initDateAndTimePicker() {

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .build()

        val timePicker =
            MaterialTimePicker.Builder().build()

        binding.imageDayEdit.setOnClickListener {
            datePicker.show(parentFragmentManager, "Date")
        }

        binding.imageTimeEdit.setOnClickListener {
            timePicker.show(parentFragmentManager, "Time")
        }

        datePicker.addOnPositiveButtonClickListener {
            val dateFormat = SimpleDateFormat("MMMM dd", Locale.getDefault())
            val dateString = dateFormat.format(it)
            viewModel.setAlarmDay(it)
            binding.monthDays.text = dateString.uppercase()
        }

        timePicker.addOnPositiveButtonClickListener {
            val time =
                timePicker.hour.toLong() * HOUR + timePicker.minute.toLong() * MINUTE
            viewModel.setAlarmTime(time)
            // replace textview text
            val amPm = if (timePicker.hour <= 12) AM else PM
            binding.hourMinute.text = resources.getString(
                R.string.a_hh_mm,
                amPm,
                timePicker.hour.formatHour(),
                timePicker.minute.formatMinutes()
            )
        }
    }
}

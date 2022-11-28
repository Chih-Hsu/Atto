package com.chihwhsu.atto.clock.alarm


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.R
import com.chihwhsu.atto.databinding.FragmentAlarmBinding
import com.chihwhsu.atto.ext.formatHour
import com.chihwhsu.atto.ext.formatMinutes
import com.chihwhsu.atto.ext.getCurrentDay
import com.chihwhsu.atto.ext.getVmFactory
import com.google.android.material.timepicker.MaterialTimePicker
import java.text.SimpleDateFormat
import java.util.*

class AlarmFragment : Fragment() {

    private lateinit var binding: FragmentAlarmBinding
    private val viewModel by viewModels<AlarmViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlarmBinding.inflate(inflater, container, false)

        viewModel.getRingTone(requireContext())

        setRoutineDays()
        setCurrentTime()
        setRingToneSpinner()
        setTimePicker()

        binding.vibrationSwitch.setOnCheckedChangeListener { _, _ ->
            viewModel.setVibration()
        }

        binding.snoozeSwitch.setOnCheckedChangeListener { _, _ ->
            viewModel.setSnooze()
        }

        binding.button.setOnClickListener {
            viewModel.saveEvent()
        }

        viewModel.event.observe(viewLifecycleOwner) {
            it.setAlarmTime(requireActivity().applicationContext, it.id)
        }

        viewModel.navigateToAlarmList.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(AlarmFragmentDirections.actionAlarmFragmentToClockFragment())
                viewModel.doneNavigation()
            }
        }



        return binding.root
    }

    private fun setCurrentTime() {
        // Set current time
        val currentTime = System.currentTimeMillis() + 10 * MINUTE
        val simpleFormat = SimpleDateFormat(TIME_PATTERN, Locale.getDefault())
        binding.hourMinute.text = simpleFormat.format(currentTime)
    }

    private fun setRingToneSpinner() {
        viewModel.ringToneList.observe(viewLifecycleOwner) {
            binding.ringtoneSpinner.adapter = ArrayAdapter(
                requireContext(),
                R.layout.item_ringtone_spinner,
                it
            )

            binding.ringtoneSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                        Adapter: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        viewModel.setRingTonePosition(position)
                    }
                }
        }
    }

    private fun setTimePicker() {

        // TimePicker
        val timePicker = MaterialTimePicker.Builder()
            .build()

        binding.imageTimeEdit.setOnClickListener {
            timePicker.show(parentFragmentManager, "TimePicker")
        }
        timePicker.apply {
            addOnPositiveButtonClickListener {

                val time =
                    getCurrentDay() + hour.toLong() * HOUR + minute.toLong() * MINUTE

                viewModel.setAlarmTime(time)

                // replace textview text
                val amPm = if (hour <= 12) "AM" else "PM"
                binding.hourMinute.text = resources.getString(
                    R.string.a_hh_mm,
                    amPm,
                    hour.formatHour(),
                    minute.formatMinutes()
                )

            }
        }
    }

    private fun setRoutineDays() {
        val list = listOf(
            binding.monday,
            binding.tuesday,
            binding.wednesday,
            binding.thursday,
            binding.friday,
            binding.saturday,
            binding.sunday
        )

        for (item in list) {
            item.setOnClickListener {
                viewModel.setWeekRepeat(list.indexOf(item))
            }
        }

        viewModel.dayList.observe(viewLifecycleOwner) {
            setBackground(list, it)
        }
    }

    private fun setBackground(viewList: List<TextView>, isSelect: List<Boolean>) {
        for (item in viewList) {
            val position = viewList.indexOf(item)
            if (isSelect[position]) {
                item.setBackgroundResource(R.drawable.week_corner_background_select)
            } else {
                item.setBackgroundResource(R.drawable.week_corner_background)
            }
        }
    }

    companion object {
        const val MINUTE = 60L * 1000L
        const val HOUR = 60L * MINUTE
        const val TIME_PATTERN = "a  hh:mm"
    }

}
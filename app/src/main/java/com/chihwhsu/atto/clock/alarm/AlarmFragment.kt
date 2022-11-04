package com.chihwhsu.atto.clock.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.R
import com.chihwhsu.atto.databinding.FragmentAlarmBinding
import com.chihwhsu.atto.ext.formatHour
import com.chihwhsu.atto.ext.formatMinutes
import com.google.android.material.timepicker.MaterialTimePicker

class AlarmFragment : Fragment() {

    private lateinit var binding: FragmentAlarmBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        viewModel.getRingTone(requireContext())

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

        viewModel.dayList.observe(viewLifecycleOwner, Observer {
            setBackground(list, it)
        })

        binding.button.setOnClickListener {
        }

        // RingTone Spinner
        binding.ringtoneSpinner.adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.item_ringtone_spinner,
            viewModel.ringToneList
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


                }
            }

        // TimePicker
        val timePicker = MaterialTimePicker.Builder()
            .build()

        binding.imageTimeEdit.setOnClickListener {
            timePicker.show(parentFragmentManager, "TimePicker")
        }

        timePicker.apply {
            addOnPositiveButtonClickListener {

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






        return binding.root
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


}
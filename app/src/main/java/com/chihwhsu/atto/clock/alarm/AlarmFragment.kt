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
import com.chihwhsu.atto.ext.toFormat
import com.google.android.material.timepicker.MaterialTimePicker
import java.text.SimpleDateFormat
import java.util.*

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

        // Set current time
        val currentTime = System.currentTimeMillis() + 600000
        val simpleFormat = SimpleDateFormat("a  hh:mm")
        binding.hourMinute.text = simpleFormat.format(currentTime)

        binding.button.setOnClickListener {
        }

        // RingTone Spinner
        viewModel.ringToneList.observe(viewLifecycleOwner, Observer {
            binding.ringtoneSpinner.adapter = ArrayAdapter<String>(
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
        })



        // TimePicker
        val timePicker = MaterialTimePicker.Builder()
            .build()

        binding.imageTimeEdit.setOnClickListener {
            timePicker.show(parentFragmentManager, "TimePicker")
        }

        timePicker.apply {
            addOnPositiveButtonClickListener {

                val time = hour.toLong()*60*60*1000 + minute.toLong()*60*1000
                val amPm = if (hour<=12)"AM" else "PM"
                viewModel.setAlarmTime(time)

                // replace textview text
                binding.hourMinute.text = resources.getString(
                    R.string.a_hh_mm,
                    amPm,
                    hour.formatHour(),
                    minute.formatMinutes()
                )

//                binding.am.text = if (timePicker.hour <= 12) {
//                    "am"
//                } else {
//                    "pm"
//                }
            }
        }
        binding.vibrationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                viewModel.setVibration()
            }
        }

        binding.snoozeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                viewModel.setSnooze()
            }
        }

        binding.button.setOnClickListener {
            viewModel.saveEvent()
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
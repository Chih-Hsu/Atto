package com.chihwhsu.atto.clock.pomodoro

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chihwhsu.atto.R
import com.chihwhsu.atto.applistpage.AppListViewModel
import com.chihwhsu.atto.databinding.FragmentPomodoroBinding
import com.chihwhsu.atto.ext.formatHour
import com.chihwhsu.atto.ext.formatMinutes
import com.chihwhsu.atto.ext.getCurrentDay
import com.chihwhsu.atto.ext.getVmFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import java.text.SimpleDateFormat

class PomodoroFragment : Fragment() {

    private lateinit var binding: FragmentPomodoroBinding
    private val viewModel by viewModels<PomodoroViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPomodoroBinding.inflate(inflater,container,false)

        // set current time
        val currentTime = System.currentTimeMillis() + 60000
        val simpleFormat = SimpleDateFormat("a hh:mm")
        binding.hourMinute.text = simpleFormat.format(currentTime)




        setTimePicker()

        binding.editWorkTime.doAfterTextChanged { number ->
            if (!number.isNullOrEmpty()) {
                viewModel.setWorkTime(number.toString().toLong())
            }
        }

        binding.editBreakTime.doAfterTextChanged { number ->
            if (!number.isNullOrEmpty()) {
                viewModel.setBreakTime(number.toString().toLong())
            }
        }

        binding.editRoutine.doAfterTextChanged { number ->
            if (!number.isNullOrEmpty()){
                viewModel.setRoutineRound(number.toString().toInt())
            }

        }

        binding.lockAppSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setLockAppMode()
        }

        binding.button.setOnClickListener {
            viewModel.saveEvent()
        }

        // Spinner
        viewModel.labelList.observe(viewLifecycleOwner, Observer {
            val setList = it.toSet()
            val newList = mutableListOf<String>()
            newList.add("全部")
            for (item in setList){
                if (!item.isNullOrEmpty() && item != "dock"){
                    newList.add(item)
                }
            }

            binding.labelSpinner.adapter = ArrayAdapter<String>(requireContext(),
                R.layout.item_ringtone_spinner,newList)

            binding.labelSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                        Adapter: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                            viewModel.setLockAppLabel(newList[position])

                    }
                }
        })







        return binding.root
    }

    private fun setTimePicker(){

        val timePicker =
            MaterialTimePicker.Builder().build()

        binding.imageTimeEdit.setOnClickListener {
            timePicker.show(parentFragmentManager,"Time")
        }

        timePicker.addOnPositiveButtonClickListener {
            // replace textview text
            val time : Long = timePicker.hour.toLong()*60*60*1000 + timePicker.minute.toLong()*60*1000
            viewModel.setBeginTime(time + getCurrentDay())
            val amPm = if (timePicker.hour<=12)"AM" else "PM"
            binding.hourMinute.text = resources.getString(
                R.string.a_hh_mm,
                amPm,
                timePicker.hour.formatHour(),
                timePicker.minute.formatMinutes()
            )

        }
    }
}
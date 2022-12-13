package com.chihwhsu.atto.clock.pomodoro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.R
import com.chihwhsu.atto.databinding.FragmentPomodoroBinding
import com.chihwhsu.atto.ext.formatHour
import com.chihwhsu.atto.ext.formatMinutes
import com.chihwhsu.atto.ext.getCurrentDay
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.util.*
import com.google.android.material.timepicker.MaterialTimePicker
import java.text.SimpleDateFormat
import java.util.*

class PomodoroFragment : Fragment() {

    private lateinit var binding: FragmentPomodoroBinding
    private val viewModel by viewModels<PomodoroViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPomodoroBinding.inflate(inflater, container, false)

        // set current time
        val currentTime = System.currentTimeMillis() + 10 * MINUTE
        val simpleFormat = SimpleDateFormat(TIME_PATTERN, Locale.getDefault())
        binding.hourMinute.text = simpleFormat.format(currentTime)

        setTimePicker()

        // Spinner
        setLabelSpinner()

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
            if (!number.isNullOrEmpty()) {
                viewModel.setRoutineRound(number.toString().toInt())
            }
        }

        binding.lockAppSwitch.setOnCheckedChangeListener { _, _ ->
            viewModel.setLockAppMode()
        }

        binding.button.setOnClickListener {
            if (!viewModel.checkCanCreate()) {
                viewModel.saveEvent(requireActivity().applicationContext)
            } else {
                Toast.makeText(requireContext(), "Pomodoro already set", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.navigateToHome.observe(viewLifecycleOwner) {
            findNavController().navigate(NavigationDirections.actionGlobalMainFragment())
        }

        return binding.root
    }

    private fun setLabelSpinner() {
        viewModel.labelList.observe(viewLifecycleOwner) {
            val setList = it.toSet()
            val newList = mutableListOf<String>()
            newList.add(ALL)
            for (item in setList) {
                if (!item.isNullOrEmpty() && item != DOCK) {
                    newList.add(item)
                }
            }

            binding.labelSpinner.adapter = ArrayAdapter(
                requireContext(),
                R.layout.item_ringtone_spinner, newList
            )

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
        }
    }

    private fun setTimePicker() {

        val timePicker =
            MaterialTimePicker.Builder().build()

        binding.imageTimeEdit.setOnClickListener {
            timePicker.show(parentFragmentManager, TAG)
        }

        timePicker.addOnPositiveButtonClickListener {
            // replace textview text
            val time: Long = timePicker.hour.toLong() * HOUR + timePicker.minute.toLong() * MINUTE
            viewModel.setBeginTime(time + getCurrentDay())
            val amPm = if (timePicker.hour <= 12) AM else PM
            binding.hourMinute.text = resources.getString(
                R.string.a_hh_mm,
                amPm,
                timePicker.hour.formatHour(),
                timePicker.minute.formatMinutes()
            )
        }
    }

    companion object {
        private const val TAG = "Time"
    }
}

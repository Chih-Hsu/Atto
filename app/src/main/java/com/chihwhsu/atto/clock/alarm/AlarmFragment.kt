package com.chihwhsu.atto.clock.alarm


import android.app.AlertDialog
import android.app.Service
import android.content.DialogInterface
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.R
import com.chihwhsu.atto.databinding.FragmentAlarmBinding
import com.chihwhsu.atto.ext.formatHour
import com.chihwhsu.atto.ext.formatMinutes
import com.chihwhsu.atto.ext.getCurrentDay
import com.chihwhsu.atto.ext.getVmFactory
import com.google.android.material.timepicker.MaterialTimePicker
import java.text.SimpleDateFormat

class AlarmFragment : Fragment() {

    private lateinit var binding: FragmentAlarmBinding
    private val viewModel by viewModels<AlarmViewModel> { getVmFactory() }

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmBinding.inflate(inflater, container, false)

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

                val time = getCurrentDay() + hour.toLong()*60*60*1000 + minute.toLong()*60*1000

                viewModel.setAlarmTime(time)

                // replace textview text
                val amPm = if (hour<=12)"AM" else "PM"
                binding.hourMinute.text = resources.getString(
                    R.string.a_hh_mm,
                    amPm,
                    hour.formatHour(),
                    minute.formatMinutes()
                )

            }
        }
        binding.vibrationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.setVibration()
        }

        binding.snoozeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.setSnooze()
        }

        binding.button.setOnClickListener {
            viewModel.saveEvent()
        }

        viewModel.event.observe(viewLifecycleOwner, Observer {
            it.setAlarmTime(requireContext())
            Log.d("alarm","step 1 work")
        })

        viewModel.navigateToAlarmList.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(AlarmFragmentDirections.actionAlarmFragmentToClockFragment())
                viewModel.doneNavigation()
            }
        })


        //////
        val intent = requireActivity().intent
        val message = intent.getStringExtra("msg")
        val flag = intent.getIntExtra("flag",0)
        showDialogInBroadcastReceiver(message,flag)





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

    fun showDialogInBroadcastReceiver(message:String?,flag:Int){
        if (flag == 1 || flag == 2) {
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.in_call_alarm)
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }
        //数组参数意义：第一个参数为等待指定时间后开始震动，震动时间为第二个参数。后边的参数依次为等待震动和震动的时间
        //第二个参数为重复次数，-1为不重复，0为一直震动
        //数组参数意义：第一个参数为等待指定时间后开始震动，震动时间为第二个参数。后边的参数依次为等待震动和震动的时间
        //第二个参数为重复次数，-1为不重复，0为一直震动
        if (flag == 0 || flag == 2) {
            vibrator = requireActivity().getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(longArrayOf(100, 10, 100, 600), 0)
        }

        val dialog = AlertDialog.Builder(requireContext(),R.style.Theme_dialog)
            .setTitle("Wake Up")
            .setMessage(message)
            .setPositiveButton("Stop",object :DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    if (flag == 1 || flag == 2) {
                        mediaPlayer.stop()
                        mediaPlayer.release()
                    }
                    if (flag == 0 || flag == 2) {
                        vibrator.cancel()
                    }
                    dialog?.dismiss()
                }
            })
            .create()

        dialog.show()



    }




}
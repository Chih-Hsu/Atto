package com.chihwhsu.atto.clock.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.clock.ClockFragmentDirections
import com.chihwhsu.atto.databinding.FragmentAlarmListBinding
import com.chihwhsu.atto.ext.getVmFactory

class AlarmListFragment : Fragment() {

    private val viewModel by viewModels<AlarmListViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAlarmListBinding.inflate(inflater,container,false)


        val adapter =  AlarmAdapter(AlarmAdapter.AlarmOnClickListener {
            viewModel.deleteEvent(it.id)
        },AlarmAdapter.AlarmCheckChangeListener { isChecked , event ->
            if (isChecked){
                event.setAlarmTime(requireActivity().applicationContext,event.id.toInt())
            }else{
                event.stopAlarm(requireActivity().applicationContext)
            }
        })
        binding.recyclerviewAlarm.adapter = adapter

        viewModel.alarmList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(ClockFragmentDirections.actionClockFragmentToAlarmFragment())
        }



        return binding.root
    }
}
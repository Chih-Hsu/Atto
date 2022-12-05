package com.chihwhsu.atto.timezonepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.databinding.FragmentTimezoneBinding
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.util.UserPreference

class TimeZoneFragment : Fragment() {

    private lateinit var binding: FragmentTimezoneBinding
    private val viewModel by viewModels<TimeZoneViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTimezoneBinding.inflate(inflater, container, false)

        setClockDisplayMode()

        val adapter = TimeZoneAdapter(TimeZoneAdapter.TIMEZONE_FRAGMENT)
        binding.recyclerviewTimezone.adapter = adapter
        setItemTouchHelper(adapter)

        viewModel.timeZoneList.observe(viewLifecycleOwner, androidx.lifecycle.Observer { list ->
            adapter.submitList(list.sortedBy { it.sort })
        })


        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(TimeZoneFragmentDirections.actionTimeZoneFragmentToTimeZoneDialog())
        }


        return binding.root
    }

    private fun setClockDisplayMode() {
        if (UserPreference.showSingleTimeZoneClock){
            binding.singleCheckbox.isChecked = true
        } else {
            binding.multiCheckbox.isChecked = true
        }


        binding.singleCheckbox.addOnCheckedStateChangedListener { checkBox, state ->
            if (checkBox.isChecked){
                binding.multiCheckbox.isChecked = false
                UserPreference.showSingleTimeZoneClock = true
            }
        }
        binding.multiCheckbox.addOnCheckedStateChangedListener { checkBox, state ->
            if (checkBox.isChecked){
                binding.singleCheckbox.isChecked = false
                UserPreference.showSingleTimeZoneClock = false
            }
        }
    }

    private fun setItemTouchHelper(adapter: TimeZoneAdapter) {
        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
            100
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                adapter.notifyItemMoved(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

//                adapter.notifyItemRemoved(position)
                viewModel.remove(adapter.currentList[position])
//                adapter.notifyDataSetChanged()
            }
        }
        val itemHelper = ItemTouchHelper(simpleCallback)
        itemHelper.attachToRecyclerView(binding.recyclerviewTimezone)
    }
}
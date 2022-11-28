package com.chihwhsu.atto.timezonepage.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.applistpage.AppListViewModel
import com.chihwhsu.atto.databinding.DialogTimezoneBinding
import com.chihwhsu.atto.ext.getVmFactory
import java.util.*

class TimeZoneDialog : DialogFragment() {

    private val viewModel by viewModels<TimeZoneDialogViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogTimezoneBinding.inflate(inflater,container,false)

        val adapter = TimeZoneDialogAdapter(
            TimeZoneDialogAdapter.TimeZoneClickListener {
                viewModel.insert(it)
            }
        )
        binding.recyclerviewTimezoneName.adapter = adapter

        viewModel.timeZoneIds.observe(viewLifecycleOwner, androidx.lifecycle.Observer { list ->
            adapter.submitList(list)
        })

        viewModel.navigateUp.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true){
                findNavController().navigateUp()
            }
        })


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }
}
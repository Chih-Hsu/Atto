package com.chihwhsu.atto.usagelimit

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.R
import com.chihwhsu.atto.databinding.DialogUsageLimitBinding
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.util.clickAnimation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UsageLimitDialog : BottomSheetDialogFragment() {

    private val viewModel by viewModels<UsageLimitViewModel> { getVmFactory() }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        // Transparent background
        bottomSheetDialog.setOnShowListener {
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
        }

        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogUsageLimitBinding.inflate(inflater, container, false)
        val app = UsageLimitDialogArgs.fromBundle(requireArguments()).app

        binding.hourArrowUp.setOnClickListener {
            it.clickAnimation(requireContext(), R.anim.arrow_up_anim)
            val hour = binding.textHour.text.toString().toInt()
            viewModel.addHour(hour)
        }

        binding.hourArrowDown.setOnClickListener {
            it.clickAnimation(requireContext(), R.anim.arrow_down_anim)
            val hour = binding.textHour.text.toString().toInt()
            viewModel.subHour(hour)
        }

        binding.minuteArrowUp.setOnClickListener {
            it.clickAnimation(requireContext(), R.anim.arrow_up_anim)
            val minutes = binding.textMinutes.text.toString().toInt()
            viewModel.addMinutes(minutes)
        }

        binding.minuteArrowDown.setOnClickListener {
            it.clickAnimation(requireContext(), R.anim.arrow_down_anim)
            val minutes = binding.textMinutes.text.toString().toInt()
            viewModel.subMinutes(minutes)
        }

        binding.buttonSend.setOnClickListener {
            viewModel.lockApp(app)
        }

        binding.buttonClose.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.newHour.observe(
            viewLifecycleOwner,
            Observer {
                binding.textHour.text = it.toString()
            }
        )

        viewModel.newMinutes.observe(
            viewLifecycleOwner,
            Observer {
                binding.textMinutes.text = it.toString()
            }
        )

        binding.buttonSend.setOnClickListener {
            viewModel.lockApp(app)
            findNavController().navigateUp()
        }

        return binding.root
    }
}

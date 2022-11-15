package com.chihwhsu.atto.appinfodialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.chihwhsu.atto.databinding.DialogAppInfoBinding

class AppInfoDialog : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogAppInfoBinding.inflate(inflater,container,false)

        val app = AppInfoDialogArgs.fromBundle(requireArguments()).app

//        binding.iconImage.setImageBitmap(app.icon)
        Glide.with(requireContext()).load(app.iconPath).into(binding.iconImage)
        binding.limitButton.setOnClickListener {
            findNavController().navigate(AppInfoDialogDirections.actionAppInfoDialogToUsageLimitDialog(app))
        }

        binding.dashboardButton.setOnClickListener {
            findNavController().navigate(AppInfoDialogDirections.actionAppInfoDialogToAppDetailFragment(app))
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }
}
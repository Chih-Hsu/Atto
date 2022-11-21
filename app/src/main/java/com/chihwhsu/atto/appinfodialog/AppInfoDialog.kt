package com.chihwhsu.atto.appinfodialog


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.chihwhsu.atto.applistpage.bottomsheet.AppListBottomViewModel
import com.chihwhsu.atto.databinding.DialogAppInfoBinding
import com.chihwhsu.atto.ext.getVmFactory

class AppInfoDialog : DialogFragment() {

    private val viewModel by viewModels<AppInfoViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogAppInfoBinding.inflate(inflater,container,false)

        val app = AppInfoDialogArgs.fromBundle(requireArguments()).app


        Glide.with(requireContext()).load(app.iconPath).into(binding.iconImage)

        if (app.installed) {

            binding.limitButton.alpha = 1F
            binding.dashboardButton.alpha = 1F
            binding.removeButton.alpha = 0.5F

            binding.limitButton.setOnClickListener {
                findNavController().navigate(
                    AppInfoDialogDirections.actionAppInfoDialogToUsageLimitDialog(
                        app
                    )
                )
            }

            binding.dashboardButton.setOnClickListener {
                findNavController().navigate(
                    AppInfoDialogDirections.actionAppInfoDialogToAppDetailFragment(
                        app
                    )
                )
            }
        }else{

            binding.limitButton.alpha = 0.5F
            binding.dashboardButton.alpha = 0.5F
            binding.removeButton.alpha = 1F

            binding.removeButton.setOnClickListener {
                viewModel.removeApp(app)
            }
        }

        viewModel.navigateUp.observe(viewLifecycleOwner, Observer {
            findNavController().navigateUp()
        })









        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }
}
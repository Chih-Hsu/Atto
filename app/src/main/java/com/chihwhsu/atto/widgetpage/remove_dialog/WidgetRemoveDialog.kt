package com.chihwhsu.atto.widgetpage.remove_dialog

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
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.R
import com.chihwhsu.atto.databinding.DialogWidgetRemoveBinding
import com.chihwhsu.atto.ext.getVmFactory

class WidgetRemoveDialog : DialogFragment() {

    private val viewModel by viewModels<WidgetRemoveViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogWidgetRemoveBinding.inflate(inflater, container, false)

        val widget = WidgetRemoveDialogArgs.fromBundle(requireArguments()).widget

        binding.textTitle.text = getString(R.string.remove_widget, widget.label)

        binding.buttonPositive.setOnClickListener {
            viewModel.removeWidget(widget)
        }

//        binding.buttonNegative.setOnClickListener {
//            findNavController().navigateUp()
//        }

        viewModel.navigateToHome.observe(
            viewLifecycleOwner,
            Observer {
                if (it == true) {
                    findNavController().navigate(NavigationDirections.actionGlobalMainFragment())
                }
            }
        )

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}

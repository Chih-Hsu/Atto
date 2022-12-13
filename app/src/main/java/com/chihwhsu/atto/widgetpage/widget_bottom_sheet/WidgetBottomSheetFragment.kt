package com.chihwhsu.atto.widgetpage.widget_bottom_sheet

import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.databinding.DialogWidgetBottomSheetBinding
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.widgetpage.WidgetAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class WidgetBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<WidgetBottomViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogWidgetBottomSheetBinding.inflate(inflater, container, false)

        val appWidgetManager = AppWidgetManager.getInstance(requireActivity().applicationContext)

        val adapter = WidgetAdapter(
            WidgetAdapter.WidgetOnClickListener {
                viewModel.saveWidget(it)
            }
        )

        viewModel.navigateUp.observe(
            viewLifecycleOwner
        ) { isStartNavigate ->
            if (isStartNavigate) {
                findNavController().navigateUp()
            }
        }

        binding.recyclerviewWidget.adapter = adapter
        adapter.submitList(appWidgetManager.installedProviders)

        return binding.root
    }
}

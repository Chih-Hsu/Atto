package com.chihwhsu.atto.widgetpage.widget_bottom_sheet

import android.appwidget.AppWidgetManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.data.Widget
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
    ): View? {
        val binding = DialogWidgetBottomSheetBinding.inflate(inflater, container, false)

//        val appWidgetHost = AppWidgetHost(requireActivity().applicationContext, WidgetFragment.HOST_ID)
        val appWidgetManager = AppWidgetManager.getInstance(requireActivity().applicationContext)
        val dataList = mutableListOf<Widget>()


//        appWidgetManager.installedProviders.forEach {
//            val name = it.label
//            val icon = it.loadIcon(
//                requireActivity().applicationContext,
//                resources.displayMetrics.density.toInt()
//            )
//
//            val image: Drawable? = it.loadPreviewImage(
//                requireActivity().applicationContext,
//                resources.displayMetrics.density.toInt()
//            )
//
//            val newWidget = Widget(name, icon, image, it.minWidth, it.minHeight)
//            dataList.add(newWidget)
//        }

        val adapter = WidgetAdapter(WidgetAdapter.WidgetOnClickListener {
            viewModel.saveWidget(it)

        })

        viewModel.navigateUp.observe(viewLifecycleOwner, Observer { startNavigate ->
//            findNavController().navigate(NavigationDirections.actionGlobalMainFragment(it))
            if (startNavigate){
                findNavController().navigateUp()
            }
        })

        binding.recyclerviewWidget.adapter = adapter
        adapter.submitList(appWidgetManager.installedProviders)

        return binding.root
    }




}
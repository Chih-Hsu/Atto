package com.chihwhsu.atto.widgetpage

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.Widget
import com.chihwhsu.atto.databinding.FragmentWidgetBinding
import com.chihwhsu.atto.ext.getVmFactory

class WidgetFragment : Fragment() {


    private lateinit var appWidgetHost: AppWidgetHost
    private lateinit var binding: FragmentWidgetBinding

    private val viewModel by viewModels<WidgetViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWidgetBinding.inflate(inflater, container, false)

        Log.d("LaunchTest", "WidgetFragment Work")

        binding.root.setOnLongClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalWidgetBottomSheetFragment())
            true
        }

        appWidgetHost = AppWidgetHost(requireActivity().applicationContext, HOST_ID)
        val appWidgetManager = AppWidgetManager.getInstance(requireActivity().applicationContext)
        appWidgetHost.startListening()

        viewModel.widgets.observe(
            viewLifecycleOwner
        ) { widgets ->

            for (widget in widgets) {

                // if Widget already display , don't add again
                if (viewModel.checkWidgetVisible(widget.label)) {
                    viewModel.setCatchWidget(widget)

                    // Get widget info
                    val widgetInfo =
                        appWidgetManager.installedProviders.first { it.label == widget.label }
                    val appWidgetId = appWidgetHost.allocateAppWidgetId()
                    val canBind =
                        appWidgetManager.bindAppWidgetIdIfAllowed(appWidgetId, widgetInfo.provider)

                    if (canBind) {
                        addWidgetView(appWidgetId, widgetInfo, widget)
                    } else {
                        getWidgetPermission(appWidgetId, widgetInfo)
                    }
                }
            }
        }


        return binding.root
    }

    private fun addWidgetView(
        appWidgetId: Int,
        widgetInfo: AppWidgetProviderInfo?,
        widget: Widget
    ) {
        val widgetView = appWidgetHost.createView(
            requireActivity().applicationContext,
            appWidgetId,
            widgetInfo
        ).apply {
            background = ResourcesCompat.getDrawable(resources,R.drawable.widget_rounded_background,null)
            setAppWidget(appWidgetId, widgetInfo)
        }

        val layoutParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        layoutParam.topMargin = MARGIN

        widgetView.setOnLongClickListener {
            viewModel.deleteWidget(widget.id)
            appWidgetHost.deleteHost()
            findNavController().navigate(
                NavigationDirections.actionGlobalWidgetRemoveDialog(
                    widget
                )
            )
            true
        }

        binding.containerWidget.addView(widgetView, layoutParam)
    }


    private fun getWidgetPermission(appWidgetId: Int, info: AppWidgetProviderInfo) {

        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_BIND).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, info.provider)
        }
        startActivityForResult(intent, REQUEST_BIND_APPWIDGET)
    }

    override fun onDestroy() {
        super.onDestroy()
        appWidgetHost.stopListening()
    }

    companion object {
        const val HOST_ID = 1
        const val REQUEST_BIND_APPWIDGET = 99
        const val MARGIN = 30
    }
}

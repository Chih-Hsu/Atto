package com.chihwhsu.atto.widgetpage

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.component.UsageTimerService
import com.chihwhsu.atto.databinding.FragmentWidgetBinding
import com.chihwhsu.atto.main.MainFragment


class WidgetFragment : Fragment() {

    companion object {
        const val HOST_ID = 1
        const val REQUEST_BIND_APPWIDGET = 99
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentWidgetBinding.inflate(inflater, container, false)

        binding.root.setOnLongClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalWidgetBottomSheetFragment())
            true
        }



        val appWidgetHost = AppWidgetHost(requireActivity().applicationContext, HOST_ID)
        val appWidgetManager = AppWidgetManager.getInstance(requireActivity().applicationContext)

        val widgetLabel = (requireParentFragment() as MainFragment).getWidgetInfo()


        widgetLabel?.let {
            val widgetInfo = appWidgetManager.installedProviders.filter { it.label == widgetLabel }.first()
            val appWidgetId = appWidgetHost.allocateAppWidgetId()
            val canBind = appWidgetManager.bindAppWidgetIdIfAllowed(appWidgetId, widgetInfo.provider)

            if (canBind) {
                val widgetView = appWidgetHost.createView(
                    requireActivity().applicationContext,
                    appWidgetId,
                    widgetInfo
                ).apply {
                    setAppWidget(appWidgetId, widgetInfo)
                }
                binding.containerWidget.addView(widgetView)
            } else {
                getWidgetPermission(appWidgetId, widgetInfo)
            }

        }

        return binding.root
    }

    fun startUsageService() {
        val serviceIntent = Intent(this.context, UsageTimerService::class.java)
        serviceIntent.putExtra("limitTime", 10000)
        requireContext().startService(serviceIntent)
    }

    private fun getWidgetPermission(appWidgetId: Int, info: AppWidgetProviderInfo) {

        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_BIND).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, info.provider)
        }
        startActivityForResult(intent, REQUEST_BIND_APPWIDGET)
    }

}
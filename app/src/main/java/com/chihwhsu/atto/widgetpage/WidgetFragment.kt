package com.chihwhsu.atto.widgetpage

import android.annotation.SuppressLint
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.component.GestureListener
import com.chihwhsu.atto.component.UsageTimerService
import com.chihwhsu.atto.databinding.FragmentWidgetBinding
import com.chihwhsu.atto.main.MainFragment


class WidgetFragment : Fragment(){

companion object{
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
//
        val widgetInfo = (requireParentFragment() as MainFragment).getWidgetInfo()
        Log.d("testwi","$widgetInfo")

        val appWidgetHost = AppWidgetHost(requireActivity().applicationContext, HOST_ID)
        val appWidgetManager = AppWidgetManager.getInstance(requireActivity().applicationContext)

        widgetInfo?.let {
            val appWidgetId = appWidgetHost.allocateAppWidgetId()
            val canBind = appWidgetManager.bindAppWidgetIdIfAllowed(appWidgetId, it.provider)

            if (canBind) {
                val widgetView = appWidgetHost.createView(
                    requireActivity().applicationContext,
                    appWidgetId,
                    it
                ).apply {
                    setAppWidget(appWidgetId, it)
                }
                binding.containerWidget.addView(widgetView)
            } else {
                getWidgetPermission(appWidgetId, it)
            }
        }







        return binding.root
    }

    fun startUsageService() {
        val serviceIntent = Intent(this.context, UsageTimerService::class.java)
        serviceIntent.putExtra("limitTime", 10000)
        requireContext().startService(serviceIntent)
    }

    fun getWidgetPermission(appWidgetId:Int,info: AppWidgetProviderInfo){

        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_BIND).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, info.provider)
            // This is the options bundle described in the preceding section.
//            putExtra(AppWidgetManager.EXTRA_APPWIDGET_OPTIONS, options)
        }

        startActivityForResult(intent, REQUEST_BIND_APPWIDGET)

    }


}
package com.chihwhsu.atto.widgetpage

import android.app.ActionBar
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.R
import com.chihwhsu.atto.component.UsageTimerService
import com.chihwhsu.atto.databinding.FragmentWidgetBinding
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.homepage.HomeViewModel
import com.chihwhsu.atto.main.MainFragment


class WidgetFragment : Fragment() {

    companion object {
        const val HOST_ID = 1
        const val REQUEST_BIND_APPWIDGET = 99
    }

    private lateinit var appWidgetHost: AppWidgetHost


    private val viewModel by viewModels<WidgetViewModel> { getVmFactory() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentWidgetBinding.inflate(inflater, container, false)

        Log.d("LaunchTest","WidgetFragment Work")

        binding.root.setOnLongClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalWidgetBottomSheetFragment())
            true
        }


        appWidgetHost = AppWidgetHost(requireActivity().applicationContext, HOST_ID)
        val appWidgetManager = AppWidgetManager.getInstance(requireActivity().applicationContext)
        appWidgetHost.startListening()


        viewModel.widgets.observe(viewLifecycleOwner, Observer { widgets ->

            for (widget in widgets) {

                if (viewModel.checkWidgetVisible(widget.label)) {
                    viewModel.setCatchWidget(widget)

                    val widgetInfo =
                        appWidgetManager.installedProviders.filter { it.label == widget.label }
                            .first()
                    val appWidgetId = appWidgetHost.allocateAppWidgetId()
                    val canBind =
                        appWidgetManager.bindAppWidgetIdIfAllowed(appWidgetId, widgetInfo.provider)

//                val viewR = RemoteViews(widgetInfo.provider.packageName,widgetInfo.initialLayout)
                    if (canBind) {
                        val widgetView = appWidgetHost.createView(
                            requireActivity().applicationContext,
                            appWidgetId,
                            widgetInfo
                        ).apply {
                            background = resources.getDrawable(R.drawable.widget_rounded_background)
//                            setAppWidget(appWidgetId, widgetInfo)
//                        layoutParams.setMargins(0, 0, 0, 0)
//                        val layoutParam = ViewGroup.LayoutParams.MATCH_PARENT
//                        updateViewLayout(this,layoutParams)
//                        updateAppWidget(viewR)
                        }
//                    binding.containerWidget.addView(widgetView)


                        val layoutParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                        layoutParam.topMargin = 30
//                        binding.containerWidget.addView(
//                            widgetView,
//                            WindowManager.LayoutParams.MATCH_PARENT,
//                            WindowManager.LayoutParams.WRAP_CONTENT
//                        )
                        binding.containerWidget.addView(
                            widgetView,
                            layoutParam
                        )


                        widgetView.setOnLongClickListener {
                            viewModel.deleteWidget(widget.id)
                            appWidgetHost.deleteHost()
                            true
                        }
//                        val layoutParam = ViewGroup.LayoutParams(
//                            WindowManager.LayoutParams.MATCH_PARENT,
//                            widgetInfo.minHeight
//                        )

//                    layoutParam.width = WindowManager.LayoutParams.MATCH_PARENT
//                    layoutParam.height = widgetInfo.minHeight

//                        binding.containerWidget.addView(widgetView, layoutParam)
                    } else {
                        getWidgetPermission(appWidgetId, widgetInfo)

                    }


                }


            }

        })


//        widgetLabel?.let {
//            val widgetInfo = appWidgetManager.installedProviders.filter { it.label == widgetLabel }.first()
//            val appWidgetId = appWidgetHost.allocateAppWidgetId()
//            val canBind = appWidgetManager.bindAppWidgetIdIfAllowed(appWidgetId, widgetInfo.provider)
//
//            if (canBind) {
//                val widgetView = appWidgetHost.createView(
//                    requireActivity().applicationContext,
//                    appWidgetId,
//                    widgetInfo
//                ).apply {
//                    setAppWidget(appWidgetId, widgetInfo)
//                }
//                binding.containerWidget.addView(widgetView)
//            } else {
//                getWidgetPermission(appWidgetId, widgetInfo)
//            }
//        }

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

    override fun onDestroy() {
        super.onDestroy()
        appWidgetHost.stopListening()
    }

}
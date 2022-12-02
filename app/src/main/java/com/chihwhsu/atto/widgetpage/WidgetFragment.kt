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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.R
import com.chihwhsu.atto.databinding.FragmentWidgetBinding
import com.chihwhsu.atto.ext.getVmFactory


class WidgetFragment : Fragment() {

    companion object {
        const val HOST_ID = 1
        const val REQUEST_BIND_APPWIDGET = 99
    }

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
                            setAppWidget(appWidgetId, widgetInfo)

                        }
//                    binding.containerWidget.addView(widgetView)


                        val layoutParam = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        layoutParam.topMargin = 30
//                        binding.containerWidget.addView(
//                            widgetView,
//                            WindowManager.LayoutParams.MATCH_PARENT,
//                            WindowManager.LayoutParams.WRAP_CONTENT
//                        )
//                        binding.containerWidget.addView(
//                            widgetView,
//                            layoutParam
//                        )


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
//                        val layoutParam = ViewGroup.LayoutParams(
//                            WindowManager.LayoutParams.MATCH_PARENT,
//                            widgetInfo.minHeight
//                        )
//
//
//                    layoutParam.width = WindowManager.LayoutParams.MATCH_PARENT
//                    layoutParam.height = widgetInfo.minHeight

                        binding.containerWidget.addView(widgetView, layoutParam)
                    } else {
                        getWidgetPermission(appWidgetId, widgetInfo)
                    }
                }
            }

        })

//        setBlurView(binding)

        return binding.root
    }


//    private fun setBlurView(binding: FragmentWidgetBinding) {
//        val radius = 7f
//        val decorView = requireActivity().window.decorView
//        // ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
//        val rootView = decorView.findViewById(android.R.id.content) as ViewGroup
//
//        // Optional:
//        // Set drawable to draw in the beginning of each blurred frame.
//        // Can be used in case your layout has a lot of transparent space and your content
//        // gets a too low alpha value after blur is applied.
//        val windowBackground = decorView.background
//
//        binding.blurView.setupWith(
//            rootView,
//            RenderScriptBlur(requireContext())
//        ) // or RenderEffectBlur
//            .setFrameClearDrawable(windowBackground) // Optional
//            .setBlurRadius(radius)
//    }


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
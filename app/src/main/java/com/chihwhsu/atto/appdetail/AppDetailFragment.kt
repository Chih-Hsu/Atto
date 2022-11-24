package com.chihwhsu.atto.appdetail


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.bumptech.glide.Glide
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.Theme
import com.chihwhsu.atto.databinding.FragmentAppDetailBinding
import com.chihwhsu.atto.ext.dpToFloat
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.ext.toFormat

class AppDetailFragment : Fragment() {

    private val viewModel by viewModels<AppDetailViewModel> {
        getVmFactory(
            AppDetailFragmentArgs.fromBundle(
                requireArguments()
            ).app
        )
    }
    private lateinit var binding: FragmentAppDetailBinding
    private lateinit var adapter: AppDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAppDetailBinding.inflate(inflater, container, false)

        setAppDetailInfo()

        viewModel.dataPerHourList.observe(viewLifecycleOwner) {
            viewModel.create24HBarSet(it)
        }

        viewModel.weekUsageList.observe(viewLifecycleOwner) {
            viewModel.createWeekChartSet(it)
        }

        viewModel.barSet.observe(viewLifecycleOwner) { barSet ->
            showBarChart(barSet)
        }

        viewModel.weekBarSet.observe(viewLifecycleOwner) { weekBarSet ->

//            binding.weekBarChart.apply {
//
//                //setting
//                spacing = 20F
//                labelsSize = dpToFloat(12)
//                labelsFormatter = { it.toInt().toString() }
//                labelsColor = resources.getColor(R.color.brown)
//
//                //show data
//                animation.duration = 1000L
//                animate(weekBarSet)
//                show(weekBarSet)
//            }
//
//        })
//
//        binding.buttonWeek.setOnClickListener {
//            binding.apply {
//                barChart.visibility = View.INVISIBLE
//                weekBarChart.visibility = View.VISIBLE
//            }
//        }
//
//        binding.buttonDay.setOnClickListener {
//            binding.apply {
//                barChart.visibility = View.VISIBLE
//                weekBarChart.visibility = View.INVISIBLE
//            }
        }

        viewModel.navigateUp.observe(viewLifecycleOwner) { canNavigate ->
            if (canNavigate) {
                viewModel.doneNavigation()
                findNavController().navigateUp()
            }
        }

        binding.detailCloseButton.setOnClickListener {
            getThemePositionAndUpdate()
        }


        return binding.root
    }

    private fun getThemePositionAndUpdate() {
        val manager = binding.backgroundRecyclerview.layoutManager as LinearLayoutManager
        val currentPosition = manager.findFirstVisibleItemPosition()
        val theme = adapter.currentList[currentPosition]
        viewModel.updateTheme(theme)
    }

    private fun showBarChart(barSet: List<Pair<String, Float>>) {
        binding.barChart.apply {

            //setting
            spacing = 20F
            labelsSize = dpToFloat(12, resources)
            labelsFormatter = { it.toInt().toString() }
            labelsColor = ResourcesCompat.getColor(resources, R.color.brown, null)

            //show data
            animation.duration = 1000L
            animate(barSet)
            show(barSet)
        }
    }

    private fun setAppDetailInfo() {

        viewModel.app.observe(viewLifecycleOwner) { app ->
            // set Title and Icon
            binding.apply {
                appName.text = app.appLabel
                Glide.with(requireContext()).load(app.iconPath).into(iconImage)
                totalUsageTime.text = app.getTotalUsage(requireContext()).toFormat()
            }

            getAppUsageTime(app)
            navigateToInfoAndDelete(app)
            setBackgroundRecyclerview(app)
        }
    }

    private fun setBackgroundRecyclerview(app: App) {
        adapter = AppDetailAdapter()
        val themeList = mutableListOf(Theme.DEFAULT, Theme.BLACK, Theme.HIGH_LIGHT, Theme.KANAHEI)
        binding.backgroundRecyclerview.adapter = adapter
        adapter.submitList(themeList)
        binding.backgroundRecyclerview.scrollToPosition(app.theme + 1)
        // LinearSnapHelper
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.backgroundRecyclerview)
    }

    private fun getAppUsageTime(app: App) {
        // get List
        viewModel.setPerHourList(app.get24HourUsageList(requireContext()))
        viewModel.setWeekList(app.getWeekUsageList(requireContext()))
    }

    private fun navigateToInfoAndDelete(app: App) {
        val appUri = Uri.fromParts("package", app.packageName, null)
        binding.appInfo.setOnClickListener {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = appUri
            startActivity(intent)
        }

        binding.appRemove.setOnClickListener {
            val intent = Intent(Intent.ACTION_DELETE, appUri)
            startActivity(intent)
        }
    }
}

package com.chihwhsu.atto.appdetail


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.Theme
import com.chihwhsu.atto.databinding.FragmentAppDetailBinding
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.ext.toFormat

class AppDetailFragment : Fragment() {

    private val viewModel by viewModels<AppDetailViewModel> { getVmFactory(AppDetailFragmentArgs.fromBundle(requireArguments()).app) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAppDetailBinding.inflate(inflater,container,false)

        val adapter = AppDetailAdapter()
        val themeList = mutableListOf<Theme>(Theme.DEFAULT,Theme.BLACK,Theme.HIGH_LIGHT,Theme.KANAHEI)

        viewModel.app.observe(viewLifecycleOwner, Observer { app ->
            // set Text
            binding.apply {
                appName.text = app.appLabel
                iconImage.setImageBitmap(app.icon)
                totalUsageTime.text = app.getTotalUsage(requireContext()).toFormat()

            }
            // get List
            viewModel.setPerHourList(app.get24HourUsageList(requireContext()))

            //
            val appUri = Uri.fromParts("package" , app.packageName , null)
            binding.appInfo.setOnClickListener {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = appUri
                startActivity(intent)
            }

            binding.appRemove.setOnClickListener {
                val intent = Intent(Intent.ACTION_DELETE,appUri)
                startActivity(intent)
            }


            binding.backgroundRecyclerview.adapter = adapter
            adapter.submitList(themeList)
            binding.backgroundRecyclerview.scrollToPosition(app.theme+1)
        })

        viewModel.dataPerHourList.observe(viewLifecycleOwner, Observer {
            viewModel.createBarSet()
        })


        viewModel.barSet.observe(viewLifecycleOwner, Observer {  barSet ->
            binding.barChart.apply {

                //setting
                spacing = 20F
                labelsSize = dpToFloat(12)
                labelsFormatter = { it.toInt().toString() }
                labelsColor = resources.getColor(R.color.brown)

                //show data
                animation.duration = 1000L
                animate(barSet)
                show(barSet)
            }
        })






        // LinearSnapHelper
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.backgroundRecyclerview)

        binding.detailCloseButton.setOnClickListener {
            val manager = binding.backgroundRecyclerview.layoutManager as LinearLayoutManager
            val currentPosition = manager.findFirstVisibleItemPosition()
            val theme = adapter.currentList.get(currentPosition)
            viewModel.updateTheme(theme)
        }

        viewModel.navigateUp.observe(viewLifecycleOwner, Observer { canNavigate ->
            if (canNavigate) {
                viewModel.doneNavigation()
                findNavController().navigateUp()
            }

        })


        return binding.root
    }

    private fun dpToFloat(dp:Int):Float{
        return resources.displayMetrics.density * dp.toFloat()
    }
}
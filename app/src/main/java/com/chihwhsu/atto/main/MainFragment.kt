package com.chihwhsu.atto.main


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chihwhsu.atto.SettingActivity
import com.chihwhsu.atto.databinding.FragmentMainBinding
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.util.UserManager

class MainFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel> { getVmFactory() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater,container,false)

        // set ViewPager2
        val adapter = MainViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

//        binding.viewPager.post {
//                binding.viewPager.setCurrentItem(1, true)
//        }

        val dockAdapter = DockAdapter(DockAdapter.DockOnClickListener { app ->

            if (app.packageName == "com.chihwhsu.atto") {
                val intent = Intent(requireContext(), SettingActivity::class.java)
                startActivity(intent)
            } else {

                if (app.installed) {
                    val launchAppIntent =
                        requireContext().packageManager.getLaunchIntentForPackage(app.packageName)
                    startActivity(launchAppIntent)

                } else {
                    // if app is not installed , then navigate to GooglePlay
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=${app.packageName}")
                    );
                    startActivity(intent)
                }
            }

        })

        binding.dockRecyclerview.adapter = dockAdapter
        viewModel.dockList.observe(viewLifecycleOwner, Observer { list ->

            if (list.isNotEmpty()){
                list.sortedBy {
                    it.sort
                }
                dockAdapter.submitList(list)
                binding.constraintLayout.visibility = View.VISIBLE
            } else {
                binding.constraintLayout.visibility = View.GONE
            }
        })


        viewModel.timerList.observe(viewLifecycleOwner, Observer {
            viewModel.checkUsageTimer(requireContext())
        })


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateApp()
    }

    override fun onDestroy() {
        if (UserManager.isLogging()){
            viewModel.uploadData(requireContext())
        }
        super.onDestroy()


    }


}
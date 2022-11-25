package com.chihwhsu.atto.main


import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.R
import com.chihwhsu.atto.SettingActivity
import com.chihwhsu.atto.applistpage.bottomsheet.AppListBottomFragment
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.databinding.FragmentMainBinding
import com.chihwhsu.atto.ext.dpToFloat
import com.chihwhsu.atto.ext.getVmFactory
import com.google.firebase.auth.FirebaseAuth
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder

class MainFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel> { getVmFactory() }
    private lateinit var binding: FragmentMainBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lottieLoading.visibility = View.VISIBLE

        setSlideDrawer()


        // set ViewPager2
        val adapter = MainViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        val dockAdapter = DockAdapter(DockAdapter.DockOnClickListener { app ->

            navigateByPackageName(app)

        })

        binding.dockRecyclerview.adapter = dockAdapter
        viewModel.dockList.observe(viewLifecycleOwner, Observer { list ->

            if (list.isNotEmpty()) {
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

    private fun setSlideDrawer() {
        val drawerBinding = binding.drawer
        drawerBinding.apply {

            textUser.text = FirebaseAuth.getInstance().currentUser?.displayName ?: ""
            linearAlarm.setOnClickListener {
                findNavController().navigate(NavigationDirections.actionGlobalClockFragment())
            }
            linearSetting.setOnClickListener {
                val intent = Intent(requireContext(),SettingActivity::class.java)
                startActivity(intent)
            }


        }

    }


    override fun onResume() {
        super.onResume()

        binding.lottieLoading.visibility = View.GONE
        viewModel.updateApp()
    }

    override fun onDestroy() {

        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            viewModel.uploadData(requireContext())
        }
        super.onDestroy()


    }

    private fun navigateByPackageName(app: App) {
        if (app.packageName == AppListBottomFragment.MY_PACKAGE_NAME) {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        } else {
            if (app.installed) {
                val launchAppIntent =
                    requireContext().packageManager.getLaunchIntentForPackage(app.packageName)
                startActivity(launchAppIntent)

            } else {
                // if app is not installed ,  navigate to GooglePlay
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("${AppListBottomFragment.GOOGLE_PLAY_LINK}id=${app.packageName}")
                )
                startActivity(intent)
            }
        }
    }


}
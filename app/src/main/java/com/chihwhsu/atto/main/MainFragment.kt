package com.chihwhsu.atto.main

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.R
import com.chihwhsu.atto.SettingActivity
import com.chihwhsu.atto.applistpage.bottomsheet.AppListBottomFragment
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.databinding.FragmentMainBinding
import com.chihwhsu.atto.ext.getVmFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

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
        setViewPagerIndicator()

        val dockAdapter = DockAdapter(
            DockAdapter.DockOnClickListener { app ->
                navigateByPackageName(app)
            }
        )

        binding.dockRecyclerview.adapter = dockAdapter
        viewModel.dockList.observe(viewLifecycleOwner) { list ->

            if (list.isNotEmpty()) {
                list.sortedBy {
                    it.sort
                }
                dockAdapter.submitList(list)
                binding.constraintLayout.visibility = View.VISIBLE
            } else {
                binding.constraintLayout.visibility = View.GONE
            }
        }

        viewModel.timerList.observe(viewLifecycleOwner) {
            viewModel.checkUsageTimer(requireContext())
        }

        // Only run this function in first start
        viewModel.appNumber.observe(viewLifecycleOwner) {
            if (it == 0) {
                viewModel.updateApp()
            }
        }

        return binding.root
    }

    private fun setViewPagerIndicator() {

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val list =
                    listOf(binding.dot1, binding.dot2, binding.dot3, binding.dot4, binding.dot5)

                when (binding.viewPager.currentItem) {

                    0 -> {
                        list.forEach { it.setBackgroundResource(R.drawable.default_dot) }
                        list[0].setBackgroundResource(R.drawable.select_dot)
                    }

                    1 -> {
                        list.forEach { it.setBackgroundResource(R.drawable.default_dot) }
                        list[1].setBackgroundResource(R.drawable.select_dot)
                        list[4].setBackgroundResource(R.drawable.select_dot)
                    }

                    2 -> {
                        list.forEach { it.setBackgroundResource(R.drawable.default_dot) }
                        list[2].setBackgroundResource(R.drawable.select_dot)
                    }

                    3 -> {
                        list.forEach { it.setBackgroundResource(R.drawable.default_dot) }
                        list[3].setBackgroundResource(R.drawable.select_dot)
                    }

                    else -> {
                        list.forEach { it.setBackgroundResource(R.drawable.default_dot) }
                    }
                }
            }
        })
    }

    private fun setSlideDrawer() {
        val drawerBinding = binding.drawer
        drawerBinding.apply {

            val user = FirebaseAuth.getInstance().currentUser
            textUser.text = user?.displayName ?: ""

            Log.d("test", "fragment user = $user")
            if (user == null) {

                textLoggin.text = getString(R.string.log_in)
                linearLoggin.setOnClickListener {
                    val intent = Intent(requireContext(), SettingActivity::class.java)
                    startActivity(intent)
                }
            } else {
                textLoggin.text = getString(R.string.log_out)
                linearLoggin.setOnClickListener {
//                    val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                        .requestIdToken(getString(R.string.default_web_client_id))
//                        .requestEmail()
//                        .build()
//
//                    val signInClient = GoogleSignIn.getClient(requireActivity(), options)
//                    signInClient.signOut()
//                    val auth = FirebaseAuth.getInstance()
//                    auth.signOut()
                    viewModel.logOut(requireContext())

                    requireActivity().recreate()
                }
            }
            linearAlarm.setOnClickListener {
                findNavController().navigate(NavigationDirections.actionGlobalClockFragment())
            }

            linearSetting.setOnClickListener {
                val intent = Intent(requireContext(), SettingActivity::class.java)
                startActivity(intent)
            }

            linearClock.setOnClickListener {
                findNavController().navigate(NavigationDirections.actionGlobalTimeZoneFragment())
            }

            textMyHa.setOnClickListener {
                navigateAdvertiseApp(MY_HA)
            }

            textMiru.setOnClickListener {
                navigateAdvertiseApp(MIRU_HIRU)
            }

            textBornMeme.setOnClickListener {
                navigateAdvertiseApp(BORN_MEME)
            }

            textPinpisode.setOnClickListener {
                navigateAdvertiseApp(PINPISODE)
            }

            textMinMap.setOnClickListener {
                navigateAdvertiseApp(MIN_MAP)
            }
        }

        setTransitionListener()
    }

    override fun onResume() {
        super.onResume()

        binding.lottieLoading.visibility = View.GONE
        viewModel.updateApp()
    }

    override fun onDestroy() {

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        user?.email?.let {
            viewModel.uploadData(requireContext(), it)
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

    private fun setTransitionListener() {
        binding.motion.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                if (startId == R.id.start) {
                    ObjectAnimator.ofFloat(
                        binding.drawerArrow,
                        ROTATION,
                        0f,
                        180F
                    ).apply {
                        duration = 500
                        start()
                    }
                } else {
                    ObjectAnimator.ofFloat(
                        binding.drawerArrow,
                        ROTATION,
                        180f,
                        360F
                    ).apply {
                        duration = 500
                        start()
                    }
                }
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }
        })
    }

    private fun navigateAdvertiseApp(packageName: String) {
        if (isPackageInstalled(packageName, requireContext().packageManager)) {
            val launchAppIntent =
                requireContext().packageManager.getLaunchIntentForPackage(packageName)
            startActivity(launchAppIntent)
        } else {
            // if app is not installed ,  navigate to GooglePlay
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("${AppListBottomFragment.GOOGLE_PLAY_LINK}id=$packageName")
            )
            startActivity(intent)
        }
    }

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: NameNotFoundException) {
            false
        }
    }

    companion object {
        private const val MY_HA = "com.cleo.myha"
        private const val MIRU_HIRU = "com.neil.miruhiru"
        private const val BORN_MEME = "com.beva.bornmeme"
        private const val PINPISODE = "com.tzuhsien.immediat"
        private const val MIN_MAP = "com.mindyhsu.minmap"

        private const val ROTATION = "rotation"
    }
}

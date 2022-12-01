package com.chihwhsu.atto.main


import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
import com.google.android.material.tabs.TabLayoutMediator
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
//        binding.viewPager.setCurrentItem(3,false)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("test","${binding.viewPager.currentItem}")
                when(binding.viewPager.currentItem){
                    0 -> {
                        binding.dot1.setBackgroundResource(R.drawable.select_dot)
                        binding.dot2.setBackgroundResource(R.drawable.default_dot)
                        binding.dot3.setBackgroundResource(R.drawable.default_dot)
                        binding.dot4.setBackgroundResource(R.drawable.default_dot)
                        binding.dot5.setBackgroundResource(R.drawable.default_dot)
                    }

                    1-> {
                        binding.dot1.setBackgroundResource(R.drawable.default_dot)
                        binding.dot2.setBackgroundResource(R.drawable.select_dot)
                        binding.dot3.setBackgroundResource(R.drawable.default_dot)
                        binding.dot4.setBackgroundResource(R.drawable.default_dot)
                        binding.dot5.setBackgroundResource(R.drawable.select_dot)
                    }
                    2->{
                        binding.dot1.setBackgroundResource(R.drawable.default_dot)
                        binding.dot2.setBackgroundResource(R.drawable.default_dot)
                        binding.dot3.setBackgroundResource(R.drawable.select_dot)
                        binding.dot4.setBackgroundResource(R.drawable.default_dot)
                        binding.dot5.setBackgroundResource(R.drawable.default_dot)
                    }
                    3-> {
                        binding.dot1.setBackgroundResource(R.drawable.default_dot)
                        binding.dot2.setBackgroundResource(R.drawable.default_dot)
                        binding.dot3.setBackgroundResource(R.drawable.default_dot)
                        binding.dot4.setBackgroundResource(R.drawable.select_dot)
                        binding.dot5.setBackgroundResource(R.drawable.default_dot)
                    }
                    else-> {
                        binding.dot1.setBackgroundResource(R.drawable.default_dot)
                        binding.dot2.setBackgroundResource(R.drawable.default_dot)
                        binding.dot3.setBackgroundResource(R.drawable.default_dot)
                        binding.dot4.setBackgroundResource(R.drawable.default_dot)
                        binding.dot5.setBackgroundResource(R.drawable.default_dot)
                    }
                }
            }
        })



//        val myFragment = parentFragment.findFragmentByTag("f" + viewpager.currentItem)



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

        // Only run this function in first start
        viewModel.appNumber.observe(viewLifecycleOwner, Observer {
            if (it == 0) {
                viewModel.updateApp()
            }
        })


        return binding.root
    }


    private fun setSlideDrawer() {
        val drawerBinding = binding.drawer
        drawerBinding.apply {

            val user = FirebaseAuth.getInstance().currentUser
            textUser.text = user?.displayName ?: ""

            if (user == null) {
                textLoggin.text = "LOG IN"
                linearLoggin.setOnClickListener {
                    val intent = Intent(requireContext(), SettingActivity::class.java)
                    startActivity(intent)
                }
            }else{
                textLoggin.text = "LOG OUT"
                linearLoggin.setOnClickListener {
                    val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()

                    val signInClient = GoogleSignIn.getClient(requireActivity(), options)
                    signInClient.signOut()
                    val  auth = FirebaseAuth.getInstance()
                    auth.signOut()

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
                val launchAppIntent =
                    requireContext().packageManager.getLaunchIntentForPackage("com.cleo.myha")
                startActivity(launchAppIntent)
            }

            textMiru.setOnClickListener {
                val launchAppIntent =
                    requireContext().packageManager.getLaunchIntentForPackage("com.neil.miruhiru")
                startActivity(launchAppIntent)
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

    private fun setTransitionListener(){
        binding.motion.addTransitionListener(object :MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                if (startId == R.id.start) {
                    ObjectAnimator.ofFloat(
                        binding.drawerArrow,
                        "rotation",
                        0f,
                        180F
                    ).apply {
                        duration = 500
                        start()
                    }
                } else {
                    ObjectAnimator.ofFloat(
                        binding.drawerArrow,
                        "rotation",
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
//                while (progress == 0f) {

//                }

            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
//                if (currentId == R.id.end) {
//                    binding.drawerButton.rotation = 180f
//                }else{
//                    binding.drawerButton.rotation = 0f
//                }
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




}
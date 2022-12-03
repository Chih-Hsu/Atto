package com.chihwhsu.atto.tutorial


import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.R
import com.chihwhsu.atto.applistpage.AppListAdapter
import com.chihwhsu.atto.applistpage.AppListFragment
import com.chihwhsu.atto.applistpage.AppListViewModel
import com.chihwhsu.atto.databinding.DialogIntroApplistBinding
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.homepage.HomeViewModel
import com.chihwhsu.atto.setting.sort.SortAdapter
import com.chihwhsu.atto.util.UserPreference
import android.view.animation.AnimationUtils


class TutorialDialog : DialogFragment() {

    private lateinit var binding: DialogIntroApplistBinding
//    private val viewModel by viewModels<AppListViewModel> { getVmFactory() }
//    private lateinit var adapter: AppListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(
            STYLE_NORMAL,
            R.style.FullScreenDialogStyle
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogIntroApplistBinding.inflate(inflater, container, false)

        UserPreference.isHomeFirstTimeLaunch = false
        setTutorialAnimation()

        binding.buttonClose.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }


    private fun setTutorialAnimation() {
        binding.animationSlideup.tutorialArrow.repeatCount = 4
        binding.animationSlideup.tutorialArrow.addAnimatorListener(object :
            Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                findNavController().navigateUp()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }
        })
    }
}
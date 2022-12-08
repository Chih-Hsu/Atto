package com.chihwhsu.atto.tutorial

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.R
import com.chihwhsu.atto.databinding.DialogIntroApplistBinding
import com.chihwhsu.atto.util.UserPreference

class TutorialDialog : DialogFragment() {

    private lateinit var binding: DialogIntroApplistBinding

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

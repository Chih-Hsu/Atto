package com.chihwhsu.atto


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.databinding.DialogIntroApplistBinding
import com.chihwhsu.atto.util.UserPreference


class introDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogIntroApplistBinding.inflate(inflater, container, false)

        when (introDialogArgs.fromBundle(requireArguments()).entry) {

            "AppList" -> {
                val path = "android.resource://" + "com.chihwhsu.atto" + "/" + "${R.raw.tutorial1}"
                binding.video1.setVideoPath(path)
                binding.video1.start()

                UserPreference.isHomeFirstTimeLaunch = false

                binding.video1.setOnCompletionListener {
                    findNavController().navigateUp()
                }
            }

            "Home" -> {
                val path = "android.resource://" + "com.chihwhsu.atto" + "/" + "${R.raw.home_intro}"
                binding.video1.setVideoPath(path)
                binding.video1.start()

                UserPreference.isListFirstTimeLaunch = false

                binding.video1.setOnCompletionListener {
                    findNavController().navigateUp()
                }

            }

            else -> {
                findNavController().navigateUp()
            }
        }

        binding.buttonClose.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
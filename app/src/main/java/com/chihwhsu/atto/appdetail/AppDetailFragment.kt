package com.chihwhsu.atto.appdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chihwhsu.atto.databinding.FragmentAppDetailBinding

class AppDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAppDetailBinding.inflate(inflater,container,false)
        val app = AppDetailFragmentArgs.fromBundle(requireArguments()).app
        binding.appName.text = app.appLabel
        binding.iconImage.setImageBitmap(app.icon)


        return binding.root
    }
}
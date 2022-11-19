package com.chihwhsu.atto.sync_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.databinding.FragmentSyncBinding
import com.chihwhsu.atto.ext.getVmFactory

class SyncFragment : Fragment() {


    private val viewModel by viewModels<SyncViewModel> { getVmFactory() }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSyncBinding.inflate(inflater,container,false)
        val user = SyncFragmentArgs.fromBundle(requireArguments()).user





        binding.buttonSync.setOnClickListener {
            viewModel.getData(user,requireContext())
        }


        binding.buttonToTutorial.setOnClickListener {
            findNavController().navigate(SyncFragmentDirections.actionSyncFragmentToWallpaperFragment())
        }

        return binding.root
    }


}
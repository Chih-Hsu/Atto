package com.chihwhsu.atto.sync_page

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.data.User
import com.chihwhsu.atto.data.Widget
import com.chihwhsu.atto.databinding.FragmentSyncBinding
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.main.MainViewModel
import com.chihwhsu.atto.util.UserManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SyncFragment : Fragment() {


    private val viewModel by viewModels<SyncViewModel> { getVmFactory() }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSyncBinding.inflate(inflater,container,false)
        val user = SyncFragmentArgs.fromBundle(requireArguments()).user

        Log.d("SyncFragment","$user")

//        viewModel.setUser(user)

        binding.buttonSync.setOnClickListener {
//            viewModel.uploadData(user)
            viewModel.getData(user)

        }



//        binding.textUserName.text = userName

        binding.buttonToTutorial.setOnClickListener {
            findNavController().navigate(SyncFragmentDirections.actionSyncFragmentToWallpaperFragment())
        }

        return binding.root
    }


}
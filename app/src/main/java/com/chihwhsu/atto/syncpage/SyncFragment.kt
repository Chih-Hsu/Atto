package com.chihwhsu.atto.syncpage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.MainActivity
import com.chihwhsu.atto.R
import com.chihwhsu.atto.data.database.remote.LoadStatus
import com.chihwhsu.atto.databinding.FragmentSyncBinding
import com.chihwhsu.atto.ext.getVmFactory

class SyncFragment : Fragment() {
    private val viewModel by viewModels<SyncViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentSyncBinding.inflate(inflater, container, false)
        val userName = SyncFragmentArgs.fromBundle(requireArguments()).userName
        val userEmail = SyncFragmentArgs.fromBundle(requireArguments()).email

        viewModel.getUser(userEmail)

        binding.textHello.text = getString(R.string.hello_user, userName)

        binding.buttonToTutorial.setOnClickListener {
            findNavController().navigate(SyncFragmentDirections.actionSyncFragmentToWallpaperFragment())
        }

        binding.buttonToMain.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.buttonSync.setOnClickListener {
                viewModel.syncData(requireContext(), user)
            }
        }

        viewModel.status.observe(
            viewLifecycleOwner
        ) {
            if (it == LoadStatus.LOADING) {
                binding.lottieLoading.visibility = View.VISIBLE
            } else {
                binding.lottieLoading.visibility = View.GONE
            }
        }

        viewModel.navigateToMain.observe(
            viewLifecycleOwner
        ) {
            if (it == true) {
                val intent = Intent(this.requireActivity(), MainActivity::class.java)
                startActivity(intent)
            }
        }



        return binding.root
    }
}

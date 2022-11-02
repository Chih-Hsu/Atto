package com.chihwhsu.atto.tutorial5_set_default

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chihwhsu.atto.MainActivity
import com.chihwhsu.atto.databinding.FragmentSetDefaultBinding


class SetDefaultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSetDefaultBinding.inflate(inflater,container,false)


        binding.textSetDefault.setOnClickListener {
            openAppSystemSettings()

        }

        binding.checkButton.setOnClickListener {
            val intent = Intent(this.requireActivity(),MainActivity::class.java)
            startActivity(intent)
        }



        return binding.root
    }

    private fun openAppSystemSettings() {
        startActivity(Intent().apply {
            action = Settings.ACTION_HOME_SETTINGS
        })
    }
}
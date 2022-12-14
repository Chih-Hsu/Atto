package com.chihwhsu.atto.setting.usagepermission

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.R
import com.chihwhsu.atto.databinding.FragmentGetUsageBinding

class GetUsageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGetUsageBinding.inflate(inflater, container, false)

        binding.switchUsage.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (!isAccessGranted()) {
                    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                    startActivity(intent)
                }
                buttonView.text = getString(R.string.switch_on)
            } else {
                buttonView.text = getString(R.string.switch_off)
            }
        }

        binding.buttonNext.setOnClickListener {
            findNavController()
                .navigate(GetUsageFragmentDirections.actionGetUsageFragmentToSetDefaultFragment())
        }

        binding.buttonPrevious.setOnClickListener {
            findNavController()
                .navigate(GetUsageFragmentDirections.actionGetUsageFragmentToSortFragment())
        }

        return binding.root
    }

    private fun isAccessGranted(): Boolean {

        return try {
            val packageManager: PackageManager = requireActivity().packageManager
            val applicationInfo =
                packageManager.getApplicationInfo(requireActivity().packageName, 0)
            val appOpsManager =
                requireActivity().applicationContext.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            var mode = 0
            mode = appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                applicationInfo.uid, applicationInfo.packageName
            )
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: NameNotFoundException) {
            false
        }
    }
}

package com.chihwhsu.atto.tutorial3_sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ThemeUtils
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.chihwhsu.atto.SettingActivity
import com.chihwhsu.atto.databinding.FragmentAddLabelBinding
import com.chihwhsu.atto.tutorial2_dock.AppListAdapter
import com.google.android.material.internal.ThemeEnforcement
import java.io.ByteArrayOutputStream

class AddLabelFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAddLabelBinding.inflate(inflater,container,false)
        val settingViewModel = (requireActivity() as SettingActivity).viewModel
        settingViewModel.removeLabelList()


        val listAdapter = AddLabelListAdapter(settingViewModel, AddLabelListAdapter.AppOnClickListener {

        })
        binding.appListRecyclerview.adapter = listAdapter
        settingViewModel.labelAppList.observe(viewLifecycleOwner, Observer {
            listAdapter.submitList(it)
        })





        return binding.root
    }


}
package com.chihwhsu.atto.applistpage.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chihwhsu.atto.applistpage.AppListAdapter
import com.chihwhsu.atto.databinding.DialogAppListBinding
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.main.MainViewModel

class AppListDialog : DialogFragment() {

    private val viewModel by viewModels<AppListDialogViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogAppListBinding.inflate(inflater,container,false)

        val adapter = AppListDialogAdapter(AppListDialogAdapter.AppOnClickListener {
                val launchAppIntent = requireContext().packageManager.getLaunchIntentForPackage(it)
                startActivity(launchAppIntent)

        })

        binding.appRecyclerView.adapter = adapter

        viewModel.appList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(viewModel.resetList(it))
        })





        return binding.root
    }
}
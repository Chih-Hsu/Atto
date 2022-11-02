package com.chihwhsu.atto.applistpage.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chihwhsu.atto.databinding.DialogAppListBinding
import com.chihwhsu.atto.ext.getVmFactory

class AppListBottomFragment : Fragment() {

    private val viewModel by viewModels<AppListBottomViewModel> { getVmFactory() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val binding = DialogAppListBinding.inflate(inflater,container,false)

        val adapter = AppListBottomAdapter(AppListBottomAdapter.AppOnClickListener {
            val launchAppIntent = requireContext().packageManager.getLaunchIntentForPackage(it)
            startActivity(launchAppIntent)
        })

        binding.appRecyclerView.adapter = adapter

        viewModel.appList.observe(viewLifecycleOwner, Observer {
            viewModel.getData()
        })

        viewModel.filterList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(viewModel.toAppListItem(it))
        })

        binding.appSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterList(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })


        return binding.root
    }
}
package com.chihwhsu.atto.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chihwhsu.atto.databinding.FragmentMainBinding
import com.chihwhsu.atto.ext.getVmFactory

class MainFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater,container,false)

        // set ViewPager2
        val adapter = MainViewPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.post {
            binding.viewPager.setCurrentItem(1, true)
        }

        val dockAdapter = DockAdapter(DockAdapter.DockOnClickListener {
            val launchAppIntent = requireContext().packageManager.getLaunchIntentForPackage(it)
            startActivity(launchAppIntent)
        })
        binding.dockRecyclerview.adapter = dockAdapter
        viewModel.dockList.observe(viewLifecycleOwner, Observer { list ->
            list.sortedBy { it.sort }
            dockAdapter.submitList(list)
        })




        return binding.root
    }
}
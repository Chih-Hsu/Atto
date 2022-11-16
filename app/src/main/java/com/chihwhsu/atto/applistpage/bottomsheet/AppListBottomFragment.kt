package com.chihwhsu.atto.applistpage.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.SettingActivity
import com.chihwhsu.atto.applistpage.AppListAdapter
import com.chihwhsu.atto.databinding.DialogAppListBinding
import com.chihwhsu.atto.ext.getVmFactory
import java.util.*

class AppListBottomFragment : Fragment() {

    private val viewModel by viewModels<AppListBottomViewModel> { getVmFactory() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val binding = DialogAppListBinding.inflate(inflater, container, false)

        val adapter = AppListBottomAdapter(
            // ClickListener
            AppListBottomAdapter.AppOnClickListener {
                if (it == "com.chihwhsu.atto") {
                    val intent = Intent(requireContext(), SettingActivity::class.java)
                    startActivity(intent)
                } else {
                    val launchAppIntent =
                        requireContext().packageManager.getLaunchIntentForPackage(it)
                    startActivity(launchAppIntent)
                }
            }   // LongClickListener
            , AppListAdapter.LongClickListener { app ->
                findNavController().navigate(NavigationDirections.actionGlobalAppInfoDialog(app))
            })

        binding.appRecyclerView.adapter = adapter

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
            0
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                val arrayList = java.util.ArrayList(adapter.currentList)
                Collections.swap(arrayList, fromPosition, toPosition)



                adapter.notifyItemMoved(fromPosition, toPosition)

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }
        }

//        val itemHelper = ItemTouchHelper(simpleCallback)
//        itemHelper.attachToRecyclerView(binding.appRecyclerView)



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
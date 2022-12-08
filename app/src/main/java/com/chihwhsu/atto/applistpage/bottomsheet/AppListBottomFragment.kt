package com.chihwhsu.atto.applistpage.bottomsheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.SettingActivity
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.databinding.DialogAppListBinding
import com.chihwhsu.atto.ext.getVmFactory
import java.util.*

class AppListBottomFragment : Fragment() {

    private val viewModel by viewModels<AppListBottomViewModel> { getVmFactory() }
    private lateinit var adapter: AppListBottomAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d("LaunchTest", "AppListBottomFragment Work")

        // Change SoftInputMode
        requireActivity().window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        val binding = DialogAppListBinding.inflate(inflater, container, false)
        setRecyclerView(binding)

        viewModel.appList.observe(viewLifecycleOwner) {
            viewModel.getData()
        }

        viewModel.filterList.observe(viewLifecycleOwner) {
            adapter.submitList(viewModel.toAppListItem(it))
        }

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

    private fun setRecyclerView(binding: DialogAppListBinding) {
        adapter = AppListBottomAdapter(
            // ClickListener
            AppListBottomAdapter.AppOnClickListener { app ->
                navigateByPackageName(app)
            }, // LongClickListener
            AppListBottomAdapter.LongClickListener { app ->
                findNavController().navigate(NavigationDirections.actionGlobalAppInfoDialog(app))
            }
        )

        binding.appRecyclerView.adapter = adapter
        setItemTouchHelper()
    }

    private fun navigateByPackageName(app: App) {
        if (app.packageName == MY_PACKAGE_NAME) {
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        } else {
            if (app.installed) {
                val launchAppIntent =
                    requireContext().packageManager.getLaunchIntentForPackage(app.packageName)
                startActivity(launchAppIntent)
            } else {
                // if app is not installed ,  navigate to GooglePlay
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("${GOOGLE_PLAY_LINK}id=${app.packageName}")
                )
                startActivity(intent)
            }
        }
    }

    private fun setItemTouchHelper() {
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
                val arrayList = ArrayList(adapter.currentList)
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
    }

    companion object {
        const val MY_PACKAGE_NAME = "com.chihwhsu.atto"
        const val GOOGLE_PLAY_LINK = "market://details?"
    }
}

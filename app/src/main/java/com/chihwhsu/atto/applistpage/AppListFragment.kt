package com.chihwhsu.atto.applistpage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.SettingActivity
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.databinding.FragmentAppListBinding
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.tutorial.sort.SortAdapter

class AppListFragment : Fragment() {

    private val viewModel by viewModels<AppListViewModel> { getVmFactory() }
    private lateinit var binding: FragmentAppListBinding
    private lateinit var adapter: AppListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAppListBinding.inflate(inflater, container, false)
        Log.d("LaunchTest", "AppListFragment Work")

        setRecyclerview()

        viewModel.appList.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()){
                binding.lottieLoading.visibility = View.GONE
            }

            viewModel.resetList(list.filter { it.appLabel != MY_APP_LABEL }, requireContext())

        }

        viewModel.appGroupList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

    private fun setRecyclerview() {

        adapter = AppListAdapter(

            AppListAdapter.AppOnClickListener { app ->
                navigateByPackageName(app)
            }, AppListAdapter.LongClickListener { app ->
                findNavController().navigate(NavigationDirections.actionGlobalAppInfoDialog(app))
            }, viewModel
        )

        binding.appRecyclerView.adapter = adapter

//        setItemTouchHelper()
        setLabelSpanSize()
    }

    private fun setLabelSpanSize() {
        val layoutManager = binding.appRecyclerView.layoutManager as GridLayoutManager

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    SortAdapter.APP_ITEM_VIEW_TYPE_LABEL -> 5
                    SortAdapter.APP_ITEM_VIEW_TYPE_APP -> 1
                    else -> 1
                }
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
                if (viewHolder.itemViewType != target.itemViewType) {
                    return false
                }
                //                if (viewHolder.itemViewType != target.itemViewType){
                //                    val fromItem = adapter.currentList.get(viewHolder.adapterPosition)
                //                    val toItem = adapter.currentList.get(target.adapterPosition)
                //                    adapter.currentList.remove(adapter.currentList.get(viewHolder.adapterPosition))
                //                    adapter.currentList.add(viewHolder.adapterPosition,toItem)
                //                    adapter.currentList.remove(adapter.currentList.get(target.adapterPosition))
                //                    adapter.currentList.add(target.adapterPosition,fromItem)
                //                }
                //                val fromPosition = viewHolder.adapterPosition
                //                val toPosition = target.adapterPosition
                //                val arrayList = java.util.ArrayList(adapter.currentList)
                //                Collections.swap(adapter.currentList,fromPosition,toPosition)
                //
                //
                //                adapter.notifyItemMoved(fromPosition,toPosition)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }
        }
        val itemHelper = ItemTouchHelper(simpleCallback)
        itemHelper.attachToRecyclerView(binding.appRecyclerView)
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
                // if app is not installed , then navigate to GooglePlay
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("${GOOGLE_PLAY_LINK}id=${app.packageName}")
                )
                startActivity(intent)
            }
        }
    }

    companion object {
        const val MY_PACKAGE_NAME = "com.chihwhsu.atto"
        const val GOOGLE_PLAY_LINK = "market://details?"
        const val MY_APP_LABEL = "Atto"
    }
}
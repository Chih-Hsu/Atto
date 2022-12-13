package com.chihwhsu.atto.setting.sort.addlabel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.R
import com.chihwhsu.atto.databinding.FragmentAddLabelBinding
import com.chihwhsu.atto.ext.getVmFactory

class AddLabelFragment : Fragment() {

    private val viewModel by viewModels<AddLabelViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentAddLabelBinding.inflate(inflater, container, false)

        val editLabel = AddLabelFragmentArgs.fromBundle(requireArguments()).label

        // if from edit button
        editLabel?.let {
            viewModel.setEditLabel(it)
            binding.editTextLabel.setText(it.uppercase())
        }

        val listAdapter =
            AddLabelListAdapter(
                viewModel,
                AddLabelListAdapter.AppOnClickListener { app ->
                    viewModel.addToList(app)
                }
            )
        binding.appListRecyclerview.adapter = listAdapter

        // get apps
        viewModel.appList.observe(
            viewLifecycleOwner
        ) {
            viewModel.getData()
        }

        // if from edit, get apps with editLabel, and set background color
        viewModel.labelAppList.observe(
            viewLifecycleOwner
        ) {
            if (editLabel != null) {
                viewModel.setRemainList(it)
            }
        }

        viewModel.filterList.observe(
            viewLifecycleOwner
        ) {
            listAdapter.submitList(it)
        }

        viewModel.navigateToSort.observe(
            viewLifecycleOwner
        ) { canNavigate ->
            if (canNavigate) {
                findNavController()
                    .navigate(AddLabelFragmentDirections.actionAddLabelFragmentToSortFragment())
                viewModel.doneNavigation()
            }
        }

        // searchView
        binding.appSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.filterList(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

        binding.buttonCheck.setOnClickListener {

            val label = binding.editTextLabel.text.toString()
            if (label.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_enter_tag_name),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.updateAppLabel(label)
            }
        }

        return binding.root
    }
}

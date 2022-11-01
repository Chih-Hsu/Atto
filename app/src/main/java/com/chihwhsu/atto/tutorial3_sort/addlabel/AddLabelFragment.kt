package com.chihwhsu.atto.tutorial3_sort.addlabel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.data.database.AttoDatabase
import com.chihwhsu.atto.databinding.FragmentAddLabelBinding
import com.chihwhsu.atto.factory.AddViewModelFactory

class AddLabelFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentAddLabelBinding.inflate(inflater,container,false)
        val viewModelFactory = AddViewModelFactory(AttoDatabase.getInstance(requireContext()).attoDatabaseDao)
        val viewModel = ViewModelProvider(this,viewModelFactory).get(AddLabelViewModel::class.java)


        val listAdapter = AddLabelListAdapter(viewModel, AddLabelListAdapter.AppOnClickListener { app ->
            viewModel.addToList(app)
        })
        binding.appListRecyclerview.adapter = listAdapter
        viewModel.noLabelAppList.observe(viewLifecycleOwner, Observer {
            listAdapter.submitList(it)
        })

        binding.buttonCheck.setOnClickListener {
            val label = binding.editTextLabel.text.toString()
            viewModel.updateAppLabel(label)
        }

        viewModel.navigateToSort.observe(viewLifecycleOwner, Observer { canNavigate ->
            if (canNavigate){
                findNavController().navigate(AddLabelFragmentDirections.actionAddLabelFragmentToSortFragment())
                viewModel.doneNavigation()
            }
        })



        return binding.root
    }


}
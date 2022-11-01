package com.chihwhsu.atto.tutorial3_sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.databinding.FragmentAppSorttingBinding

class SortFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAppSorttingBinding.inflate(inflater,container,false)

        binding.addButton.setOnClickListener {
            findNavController().navigate(SortFragmentDirections.actionSortFragmentToAddLabelFragment())
        }
















        return binding.root
    }
}
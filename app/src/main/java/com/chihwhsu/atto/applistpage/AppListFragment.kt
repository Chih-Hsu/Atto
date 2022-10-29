package com.chihwhsu.atto.applistpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chihwhsu.atto.databinding.FragmentAppListBinding

class AppListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAppListBinding.inflate(inflater,container,false)



        return binding.root
    }
}
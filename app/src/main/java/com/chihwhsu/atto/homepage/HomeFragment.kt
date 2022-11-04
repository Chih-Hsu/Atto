package com.chihwhsu.atto.homepage

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.R
import com.chihwhsu.atto.component.GestureListener
import com.chihwhsu.atto.component.GridSpacingItemDecoration
import com.chihwhsu.atto.databinding.FragmentHomeBinding
import com.chihwhsu.atto.util.UsageStatesManager

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var gestureDetector: GestureDetector

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


        val adapter = EventAdapter(EventAdapter.EventClickListener { event ->
            viewModel.setEvent(event)
        })

        //RecyclerView setting
        binding.eventList.adapter = adapter
        binding.eventList.addItemDecoration(
            GridSpacingItemDecoration(
                5,
                resources.getDimensionPixelSize(R.dimen.space_event_grid),
                true
            )
        )

        viewModel.eventList.observe(viewLifecycleOwner, Observer { list ->
            adapter.submitList(list)
        })

        // set Gesture Listener
        val gestureListener = GestureListener(viewModel)
        gestureDetector = GestureDetector(requireContext(), gestureListener)
//        binding.eventDetail.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                return gestureDetector.onTouchEvent(event)
//            }
//        })

        binding.gestureArea.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                if (v == binding.eventDetail){

                }
                return gestureDetector.onTouchEvent(event)
            }
        })


        // set event detail on card
        viewModel.event.observe(viewLifecycleOwner, Observer {
//            binding.eventId.text = it.id.toString()
        })

        // show card animation
        viewModel.showCard.observe(viewLifecycleOwner, Observer { showCard ->
            if (showCard) {
                val height = binding.eventList.height
                ObjectAnimator.ofFloat(
                    binding.eventList,
                    "translationY",
                    0F,
                    0F,
                    height.toFloat() * 4 / 5
                ).apply {
                    setDuration(300)
                    start()
                }

                binding.eventDetail.visibility = View.VISIBLE
                ObjectAnimator.ofFloat(binding.eventDetail, "alpha", 0f, 1f).apply {
                    setDuration(300)
                    start()
                }


            }
        })


        viewModel.closeCard.observe(viewLifecycleOwner, Observer { close ->
            if (close == true) {
                val height = binding.eventList.height
                ObjectAnimator.ofFloat(
                    binding.eventList,
                    "translationY",
                    height.toFloat() * 4 / 5,
                    0F,
                    0F
                ).apply {
                    setDuration(300)
                    start()
                }

                ObjectAnimator.ofFloat(binding.eventDetail, "alpha", 1f, 0f).apply {
                    setDuration(300)
                    start()
                }
                binding.eventDetail.visibility = View.GONE
            }
        })

        viewModel.navigateToEdit.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(),"event is $it",Toast.LENGTH_SHORT).show()
        })

        binding.clockMinutes.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalClockFragment())
        }





        return binding.root
    }


    override fun onPause() {
        super.onPause()
        initAnimation()
    }

    private fun initAnimation() {
        binding.eventDetail.visibility = View.GONE
        val height = binding.eventList.height
        val animation = ObjectAnimator.ofFloat(
            binding.eventList,
            "translationY",
            height.toFloat() * 4 / 5,
            0F,
            0F
        )
        animation.setDuration(1000)
        animation.start()
        viewModel.initAnimation()
    }


}
package com.chihwhsu.atto.homepage

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.R
import com.chihwhsu.atto.clock.ClockFragment
import com.chihwhsu.atto.component.GestureListener
import com.chihwhsu.atto.component.GridSpacingItemDecoration
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.databinding.FragmentHomeBinding
import com.chihwhsu.atto.ext.getEndOfToday
import com.chihwhsu.atto.ext.getTimeFrom00am
import com.chihwhsu.atto.ext.getVmFactory
import com.chihwhsu.atto.ext.toFormat

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }
    private lateinit var gestureDetector: GestureDetector

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        val adapter = HomeAdapter(HomeAdapter.EventClickListener { event ->
            viewModel.setEvent(event)
        },viewModel)

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
            val filterList = mutableListOf<Event>()
            for (event in list){
                if (event.alarmTime < getEndOfToday()){
                    filterList.add(event)
                }
            }
            adapter.submitList(filterList)
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

//                if (v == binding.eventDetail){
//
//                }
                return gestureDetector.onTouchEvent(event)
            }
        })


        // set event detail on card
        viewModel.event.observe(viewLifecycleOwner, Observer { event ->
            adapter.notifyDataSetChanged()
            binding.eventTitle.text = when(event.type){
                ClockFragment.ALARM_TYPE -> "Wake Up"
                ClockFragment.TODO_TYPE -> "NEXT TODO"
                ClockFragment.POMODORO_TYPE -> "IT'S POMODORO"
                else -> "No Event"
            }

            binding.eventContent.text = when(event.type){
                ClockFragment.TODO_TYPE -> event.content
                else -> "No Content"
            }

            binding.eventAlarmTime.text = event.alarmTime.toFormat()
        })

        // show card animation
        viewModel.showCard.observe(viewLifecycleOwner, Observer { showCard ->

            if (showCard) {
                binding.gestureArea.visibility = View.VISIBLE

                val height = binding.eventList.height
                ObjectAnimator.ofFloat(
                    binding.eventList,
                    "translationY",
                    0F,
                    0F,
                    height.toFloat()*2/5
                ).apply {
                    duration = 500
                    start()
                }

                binding.eventDetail.visibility = View.VISIBLE
                ObjectAnimator.ofFloat(binding.eventDetail, "alpha", 0f, 1f).apply {
                    duration = 500
                    start()
                }


            }
        })


        viewModel.closeCard.observe(viewLifecycleOwner, Observer { close ->
            if (close == true) {
                binding.eventEditCard.visibility = View.GONE
                val height = binding.eventList.height
                ObjectAnimator.ofFloat(
                    binding.eventList,
                    "translationY",
                    height.toFloat() * 4 / 5,
                    0F,
                    0F
                ).apply {
                    duration = 600
                    start()
                }

                ObjectAnimator.ofFloat(binding.eventDetail, "alpha", 1f, 0f).apply {
                    duration = 600
                    start()
                }
                binding.eventDetail.visibility = View.GONE
            }
        })

        viewModel.navigateToEdit.observe(viewLifecycleOwner, Observer { showEdit ->
            if (showEdit){
                binding.eventEditCard.visibility = View.VISIBLE
                val animation = AnimationUtils.loadAnimation(requireContext(),R.anim.slide_down)
                binding.eventEditCard.animation = animation
                animation.start()
            } else {
                val animation = AnimationUtils.loadAnimation(requireContext(),R.anim.slide_up)
                binding.eventEditCard.animation = animation
                animation.start()
                binding.eventEditCard.visibility = View.INVISIBLE

            }

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
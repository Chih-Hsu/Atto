package com.chihwhsu.atto.homepage

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.storage.StorageManager
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.R
import com.chihwhsu.atto.component.GestureListener
import com.chihwhsu.atto.component.GridSpacingItemDecoration
import com.chihwhsu.atto.data.Event.Companion.ALARM_TYPE
import com.chihwhsu.atto.data.Event.Companion.POMODORO_BREAK_TYPE
import com.chihwhsu.atto.data.Event.Companion.POMODORO_WORK_TYPE
import com.chihwhsu.atto.data.Event.Companion.TODO_TYPE
import com.chihwhsu.atto.databinding.FragmentHomeBinding
import com.chihwhsu.atto.ext.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }
    private lateinit var gestureDetector: GestureDetector

    private var totalInternalStorage: Long = 0
    private var freeInternalStorage: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        Log.d("LaunchTest", "HomeFragment Work")

        setGestureListener()

        val adapter = HomeAdapter(HomeAdapter.EventClickListener { event ->
            viewModel.setEvent(event)
        }, viewModel)

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

            val newList =
                list.filter { it.alarmTime < getEndOfToday() && it.alarmTime > getCurrentDay() }

            val expiredList =
                list.filter { it.alarmTime < System.currentTimeMillis() + 60000 } // Delete event after 1 minute

            viewModel.deleteEvent(expiredList)

            adapter.submitList(newList)
        })


        // set event detail on card
        viewModel.event.observe(viewLifecycleOwner, Observer { event ->

            binding.eventTitle.text = when (event.type) {
                ALARM_TYPE -> "Wake Up"
                TODO_TYPE -> "NEXT TODO"
                POMODORO_WORK_TYPE -> "IT'S POMODORO"
                POMODORO_BREAK_TYPE -> "IT'S POMODORO"
                else -> "No Event"
            }

            binding.eventContent.text = when (event.type) {
                TODO_TYPE -> event.content
                else -> "No Content"
            }

            binding.eventAlarmTime.text = if (event.type == ALARM_TYPE || event.type == TODO_TYPE
            ) {
                getTimeFrom00am(event.alarmTime).toFormat()
            } else {
                event.startTime?.let {
                    getTimeFrom00am(it).toFormat()
                }
            }

            adapter.notifyDataSetChanged()

            binding.buttonDelete.setOnClickListener {
                viewModel.deleteEvent(listOf(event))
            }

            binding.buttonDelay.setOnClickListener {
                viewModel.delayEvent(event)
            }
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
                    height.toFloat() * 2 / 5
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
                    height.toFloat() * 2 / 5,
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
            if (showEdit) {
                binding.eventEditCard.visibility = View.VISIBLE
                val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down)
                binding.eventEditCard.animation = animation
                animation.start()
            } else {
                val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
                binding.eventEditCard.animation = animation
                animation.start()
                binding.eventEditCard.visibility = View.GONE
            }
        })

        binding.clockMinutes.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalClockFragment())
        }




        return binding.root
    }

    private fun setGestureListener() {

        // set Gesture Listener
        val gestureListener = GestureListener(viewModel)
        gestureDetector = GestureDetector(requireContext(), gestureListener)

        binding.gestureArea.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return gestureDetector.onTouchEvent(event)
            }
        })
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



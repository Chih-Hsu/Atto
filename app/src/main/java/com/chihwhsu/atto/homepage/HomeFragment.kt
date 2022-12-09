package com.chihwhsu.atto.homepage

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chihwhsu.atto.NavigationDirections
import com.chihwhsu.atto.R
import com.chihwhsu.atto.component.GestureListener
import com.chihwhsu.atto.component.GridSpacingItemDecoration
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.data.Event.Companion.ALARM_TYPE
import com.chihwhsu.atto.data.Event.Companion.POMODORO_BREAK_TYPE
import com.chihwhsu.atto.data.Event.Companion.POMODORO_WORK_TYPE
import com.chihwhsu.atto.data.Event.Companion.TODO_TYPE
import com.chihwhsu.atto.databinding.FragmentHomeBinding
import com.chihwhsu.atto.ext.*
import com.chihwhsu.atto.timezonepage.TimeZoneAdapter
import com.chihwhsu.atto.util.MINUTE
import com.chihwhsu.atto.util.UserPreference

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel> { getVmFactory() }
    private lateinit var gestureDetector: GestureDetector
    private lateinit var adapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d("LaunchTest", "HomeFragment Work")

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setGestureListener()
        setRecyclerView()
        setClockDisplayMode()

        // set event detail on card
        viewModel.event.observe(
            viewLifecycleOwner
        ) { event ->
            setEventDetail(event, adapter)
        }

        // show card animation when event is clicked
        viewModel.showCard.observe(
            viewLifecycleOwner
        ) { isShowCard ->
            if (isShowCard) {
                setShowCardAnimation()
            }
        }

        // close card animation when event is clicked && card is visible
        viewModel.closeCard.observe(
            viewLifecycleOwner
        ) { isClose ->
            if (isClose) {
                setCloseCardAnimation()
            }
        }

        viewModel.showEdit.observe(
            viewLifecycleOwner
        ) { isShowEdit ->
            if (isShowEdit) {
                binding.eventEditCard.visibility = View.VISIBLE
                val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down)
                binding.eventEditCard.animation = animation
                animation.start()
            } else {
                binding.eventEditCard.visibility = View.GONE
            }
        }

        binding.clockMinutes.setOnClickListener {
            findNavController().navigate(NavigationDirections.actionGlobalClockFragment())
        }

        return binding.root
    }

    private fun setClockDisplayMode() {

        // Check user setting of clock mode
        if (UserPreference.showSingleTimeZoneClock) {

            binding.apply {
                clockMinutes.visibility = View.VISIBLE
                clockMonth.visibility = View.VISIBLE
                recyclerviewMultiClock.visibility = View.GONE
            }

        } else {

            binding.apply {
                clockMinutes.visibility = View.GONE
                clockMonth.visibility = View.GONE
                recyclerviewMultiClock.visibility = View.VISIBLE
            }

            setMultiClockRecyclerView()
        }
    }

    private fun setMultiClockRecyclerView() {
        val timeAdapter = TimeZoneAdapter(TimeZoneAdapter.HOME_FRAGMENT)
        binding.recyclerviewMultiClock.adapter = timeAdapter

        viewModel.timeZoneList.observe(
            viewLifecycleOwner
        ) {
            if (it.isNotEmpty()) {
                // Most show three timezone
                timeAdapter.submitList(it.take(3))
            } else {
                binding.apply {
                    clockMinutes.visibility = View.VISIBLE
                    clockMonth.visibility = View.VISIBLE
                    recyclerviewMultiClock.visibility = View.GONE
                }
            }
        }
    }

    private fun setRecyclerView() {
        adapter = HomeAdapter(
            HomeAdapter.EventClickListener { event ->
                viewModel.setEvent(event)
            },
            viewModel
        )

        // RecyclerView setting
        binding.eventList.adapter = adapter
        binding.eventList.addItemDecoration(
            GridSpacingItemDecoration(
                resources.getDimensionPixelSize(R.dimen.space_event_grid),
            )
        )

        viewModel.eventList.observe(
            viewLifecycleOwner
        ) { list ->

            // Only show today event and coming event
            val newList =
                list.filter { it.alarmTime < getEndOfToday() && it.alarmTime > getCurrentDay() }

            adapter.submitList(newList.filter { it.type != ALARM_TYPE })

            // Delete event after 1 minute
            val expiredList =
                list.filter { it.alarmTime < System.currentTimeMillis() + MINUTE }

            viewModel.deleteEvent(expiredList)

        }
    }

    private fun setCloseCardAnimation() {
        binding.eventEditCard.visibility = View.GONE
        val height = binding.eventList.height
        ObjectAnimator.ofFloat(
            binding.eventList,
            TRANSITION_PROPERTY_NAME,
            height.toFloat() * 2 / 5,
            0F,
            0F
        ).apply {
            duration = STANDARD_DURATION
            start()
        }

        ObjectAnimator.ofFloat(binding.eventDetail, ALPHA_PROPERTY_NAME, 1f, 0f).apply {
            duration = STANDARD_DURATION
            start()
        }
        binding.eventDetail.visibility = View.GONE
    }

    private fun setShowCardAnimation() {

        binding.gestureArea.visibility = View.VISIBLE
        val height = binding.eventList.height
        ObjectAnimator.ofFloat(
            binding.eventList,
            TRANSITION_PROPERTY_NAME,
            0F,
            0F,
            height.toFloat() * 2 / 5
        ).apply {
            duration = STANDARD_DURATION
            start()
        }

        binding.eventDetail.visibility = View.VISIBLE
        ObjectAnimator.ofFloat(binding.eventDetail, ALPHA_PROPERTY_NAME, 0f, 1f).apply {
            duration = STANDARD_DURATION
            start()
        }

        // Only Show First Time
        if (UserPreference.isHomeFirstTimeLaunch) {
            findNavController().navigate(NavigationDirections.actionGlobalIntroDialog())
        }
    }

    private fun setEventDetail(
        event: Event,
        adapter: HomeAdapter
    ) {
        binding.eventTitle.text = when (event.type) {

            ALARM_TYPE -> getString(R.string.wake_up)

            TODO_TYPE -> event.title

            POMODORO_WORK_TYPE -> getString(R.string.time_to_work)

            POMODORO_BREAK_TYPE -> getString(R.string.time_to_break)

            else -> getString(R.string.no_event)
        }

        binding.eventContent.text = when (event.type) {

            TODO_TYPE -> event.content

            POMODORO_WORK_TYPE -> getString(
                R.string.pomodoro_content,
                getTimeFromStartOfDay(event.startTime!!).toFormat(),
                getTimeFromStartOfDay(event.alarmTime).toFormat()
            )

            POMODORO_BREAK_TYPE -> getString(
                R.string.pomodoro_break_content,
                getTimeFromStartOfDay(event.startTime!!).toFormat(),
                getTimeFromStartOfDay(event.alarmTime).toFormat()
            )
            else -> getString(R.string.wake_up)
        }

        binding.eventAlarmTime.text = if (event.type == ALARM_TYPE || event.type == TODO_TYPE
        ) {
            getTimeFromStartOfDay(event.alarmTime).toFormat()
        } else {
            event.startTime?.let {
                getTimeFromStartOfDay(it).toFormat()
            }
        }


        binding.buttonDelete.setOnClickListener {
            viewModel.deleteEvent(listOf(event))
        }

        binding.buttonDelay.setOnClickListener {
            viewModel.delayEvent(event)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setGestureListener() {

        // set Gesture Listener
        val gestureListener = GestureListener(viewModel)
        gestureDetector = GestureDetector(requireContext(), gestureListener)

        binding.gestureArea.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }
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
            TRANSITION_PROPERTY_NAME,
            height.toFloat() * 2 / 5,
            0F,
            0F
        )
        animation.duration = LONG_DURATION
        animation.start()
        viewModel.initAnimation()

    }

    companion object {
        private const val LONG_DURATION = 1000L
        private const val STANDARD_DURATION = 500L
        private const val TRANSITION_PROPERTY_NAME = "translationY"
        private const val ALPHA_PROPERTY_NAME = "alpha"
    }
}

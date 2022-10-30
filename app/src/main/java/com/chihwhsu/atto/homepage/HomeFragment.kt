package com.chihwhsu.atto.homepage

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.R
import com.chihwhsu.atto.component.GestureListener
import com.chihwhsu.atto.component.GridSpacingItemDecoration
import com.chihwhsu.atto.databinding.FragmentHomeBinding

class HomeFragment : Fragment() ,View.OnTouchListener{

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var gestureDetector:GestureDetector

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

//        binding.textView.setOnClickListener {
//            val bitMap = BitmapFactory.decodeResource(resources,R.drawable.wallpaper_plant)
//            WallpaperManager.getInstance(requireContext()).setBitmap(bitMap)
//        }


        val adapter = EventAdapter(EventAdapter.EventClickListener { event ->
//            binding.eventDetail.visibility = View.INVISIBLE
            viewModel.setEvent(event)
        })

        //RecyclerView setting
        binding.eventList.adapter = adapter
        binding.eventList.addItemDecoration(
            GridSpacingItemDecoration(5, resources.getDimensionPixelSize(R.dimen.space_event_grid), true))

        viewModel.eventList.observe(viewLifecycleOwner, Observer { list ->
            adapter.submitList(list)
        })


        // set event detail on card
        viewModel.event.observe(viewLifecycleOwner, Observer {
//            binding.eventId.text = it.id.toString()
        })

        // show card animation
        viewModel.showCard.observe(viewLifecycleOwner, Observer { showCard ->

            if (showCard) {
                binding.eventList.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.slide_down
                    )
                )
                binding.eventDetail.visibility = View.VISIBLE
                binding.eventDetail.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.fade_in
                    )
                )
            }
        })

        val gestureListener = GestureListener(viewModel)
        gestureDetector = GestureDetector(requireContext(),gestureListener)
        binding.eventDetail.setOnTouchListener(this)


        viewModel.closeCard.observe(viewLifecycleOwner, Observer { close ->
            if (close == true) {
                binding.eventDetail.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.fade_out
                    )
                )
                binding.eventList.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.slide_up
                    )
                )
                binding.eventDetail.visibility = View.INVISIBLE
                Log.d("gesture", "call invisible function")
            }
        })



        return binding.root
    }


    override fun onPause() {
        super.onPause()
        binding.eventDetail.visibility = View.INVISIBLE
        viewModel.initAnimation()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//        Log.d("gesture","onTouch")

//        val action = event?.action
//        if (action == MotionEvent.)
        return gestureDetector.onTouchEvent(event)
    }

}
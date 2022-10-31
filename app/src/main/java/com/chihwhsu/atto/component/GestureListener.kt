package com.chihwhsu.atto.component

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import com.chihwhsu.atto.homepage.HomeViewModel


class GestureListener(val viewModel: HomeViewModel) : GestureDetector.OnGestureListener {

    private val MIN_MOVE = 200

    class OnScrollListener(val closeCard :()->Unit){
        fun closeCard() = closeCard
    }

    override fun onDown(e: MotionEvent?): Boolean {
        Log.d("gesture","onDown")
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
        Log.d("gesture","onShow")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.d("gesture","onSingleTapUp")
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Log.d("gesture","onScroll")
//        closeCard

        return true
    }

    override fun onLongPress(e: MotionEvent?) {
        Log.d("gesture","onLongPress")
        viewModel.navigateToEdit()
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {

        if (e1!!.y - e2!!.y > MIN_MOVE) {

            viewModel.beginCloseCard()

            Log.d("gesture","move > 200")


        } else if (e1!!.y - e2!!.y < MIN_MOVE) {
//            closeCard
            Log.d("gesture","move < 200")


        }
        return true
    }


}
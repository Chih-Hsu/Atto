package com.chihwhsu.atto.component

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import com.chihwhsu.atto.homepage.HomeViewModel

class GestureListener(val viewModel: HomeViewModel) : GestureDetector.OnGestureListener {

    override fun onDown(e: MotionEvent?): Boolean {
        Log.d(TAG, "onDown")
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
        Log.d(TAG, "onShow")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Log.d(TAG, "onSingleTapUp")
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {

        Log.d(TAG, "onScroll")
        return true
    }

    override fun onLongPress(e: MotionEvent?) {
        viewModel.navigateToEdit()
    }

    override fun onFling(
        event1: MotionEvent?,
        event2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {

        if (event1!!.y - event2!!.y > MIN_MOVE) {
            viewModel.beginCloseCard()
        }
        return true
    }

    companion object {
        private const val MIN_MOVE = 100
        private const val TAG = "Gesture"
    }
}

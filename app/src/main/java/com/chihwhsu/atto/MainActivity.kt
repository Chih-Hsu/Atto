package com.chihwhsu.atto

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.chihwhsu.atto.background.ResetWorker
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // set NavigationBar color transparent
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        setResetWorker()

    }


    override fun onStop() {
        super.onStop()
        // close desktop entry activity when onStop,then setting activity can work as expect
        finish()
    }

    private fun setResetWorker(){

        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        // Run on 00:00 everyday
        dueDate.set(Calendar.HOUR_OF_DAY, 0)
        dueDate.set(Calendar.MINUTE, 0)
        dueDate.set(Calendar.SECOND, 0)

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        val dailyWorkRequest = OneTimeWorkRequestBuilder<ResetWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag("reset worker")
            .build()


        WorkManager.getInstance(this)
            .enqueue(dailyWorkRequest)
    }

}
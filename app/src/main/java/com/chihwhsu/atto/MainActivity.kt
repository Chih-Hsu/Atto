package com.chihwhsu.atto

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.chihwhsu.atto.workmanager.ResetWorker
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set NavigationBar color transparent
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val request = OneTimeWorkRequest.Builder(ResetWorker::class.java).build()
        WorkManager.getInstance(AttoApplication.instance.applicationContext).enqueue(request)

        WorkManager.getInstance(AttoApplication.instance.applicationContext)
            .getWorkInfoByIdLiveData(request.id).observe(this) { workInfo ->
                if (workInfo != null && workInfo.state.isFinished) {
                    Log.d("WorkManager", "Success")
                }
            }

    }

    override fun onBackPressed() {
        findNavController(R.id.fragment_container_view).navigate(NavigationDirections.actionGlobalMainFragment())
    }

}

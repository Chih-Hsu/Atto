package com.chihwhsu.atto.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.data.database.AttoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AttoRepository) : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val dockList = repository.getSpecificLabelApps("dock")
    val timerList = repository.getAllTimer()


    fun checkUsageTimer(context: Context) {
        Log.d("applock","list = ${timerList.value}")
        timerList.value?.let { timers ->

            for (timer in timers) {

                coroutineScope.launch(Dispatchers.IO) {

                val app = repository.getApp(timer.packageName)
                app?.let {
                    Log.d("applock","app = ${app.appLabel} timer = ${timer.targetTime}")
                    val usageTime = it.getUsageTimeFromStart(context, timer.startTime)
                    if ( usageTime >= timer.targetTime) {
                        repository.lockApp(it.packageName)
                        repository.deleteTimer(timer.id)
                    }else{
                        repository.updateTimer(timer.targetTime - usageTime)
                    }
                }
                }
            }
        }
    }
}
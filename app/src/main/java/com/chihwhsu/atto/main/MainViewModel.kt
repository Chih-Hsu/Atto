package com.chihwhsu.atto.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.AttoApplication
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.data.database.AttoRepository
import kotlinx.coroutines.*

class MainViewModel(private val repository: AttoRepository) : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    var dockList = repository.getSpecificLabelApps("dock")
    val timerList = repository.getAllTimer()

    init {

    }

    fun checkUsageTimer(context: Context) {

        timerList.value?.let { timers ->

            coroutineScope.launch(Dispatchers.Default) {

                for (timer in timers) {

                    val app = repository.getApp(timer.packageName)

                    app?.let {

                        val usageTime = it.getUsageTimeFromStart(context, timer.startTime)
                        if (usageTime >= timer.targetTime) {

                            repository.lockApp(it.packageName)
                            repository.deleteTimer(timer.id)

                        } else {

                            repository.updateTimer(timer.targetTime - usageTime)

                        }
                    }
                }
            }
        }
    }

    fun updateApp() {

        coroutineScope.launch(Dispatchers.IO) {
            AttoApplication.instance.attoRepository.updateAppData()
        }
    }
}
package com.chihwhsu.atto.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.Result
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.data.database.remote.LoadStatus
import kotlinx.coroutines.*

class MainViewModel(private val repository: AttoRepository) : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _appNumber = MutableLiveData<Int>()
    val appNumber: LiveData<Int> get() = _appNumber

    private val _status = MutableLiveData<LoadStatus>()

    val status: LiveData<LoadStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()

    val error: LiveData<String?>
        get() = _error

    var dockList = repository.getSpecificLabelApps(DOCK)

    val timerList = repository.getAllTimer()


    init {
        getAppCount()
    }

    private fun getAppCount() {
        coroutineScope.launch(Dispatchers.Default) {
            val number = repository.getAppDataCount()

            withContext(Dispatchers.Main) {
                _appNumber.value = number
            }

        }
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
            repository.updateAppData()
        }
    }

    fun uploadData(context: Context, email: String) {

        _status.value = LoadStatus.LOADING

        coroutineScope.launch(Dispatchers.Default) {

            val localAppList = repository.getAllAppNotLiveData()

            localAppList?.let { appList ->
                when (val result = repository.uploadData(context, appList, email)) {

                    is Result.Success -> {
                        _status.value = LoadStatus.DONE
                        _error.value = null
                    }

                    is Result.Fail -> {
                        _status.value = LoadStatus.ERROR
                        _error.value = result.error
                    }

                    is Result.Error -> {
                        _status.value = LoadStatus.ERROR
                        _error.value = result.exception.toString()
                    }

                    else -> {
                        _status.value = LoadStatus.ERROR

                    }
                }
            }
        }
    }


    companion object {
        private const val DOCK = "dock"
    }
}
package com.chihwhsu.atto.setting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.database.AttoRepository
import kotlinx.coroutines.*

class SettingViewModel(val repository: AttoRepository) : ViewModel() {

    private var _appNumber = MutableLiveData<Int>()
    val appNumber: LiveData<Int> get() = _appNumber

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getAppCount()
    }

    private fun getAppCount() {
        coroutineScope.launch(Dispatchers.Default) {
            val number = repository.getAppDataCount()
            Log.d("Setting", "$number")
            withContext(Dispatchers.Main) {
                _appNumber.value = number
            }
        }
    }

    fun updateApp() {
        coroutineScope.launch(Dispatchers.IO) {
            repository.updateAppData()
        }
    }
}

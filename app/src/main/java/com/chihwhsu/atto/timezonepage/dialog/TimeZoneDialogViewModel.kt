package com.chihwhsu.atto.timezonepage.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.AttoTimeZone
import com.chihwhsu.atto.data.database.AttoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class TimeZoneDialogViewModel(val repository: AttoRepository) : ViewModel() {

    private var _timeZoneIds = MutableLiveData<List<String>>()
    val timeZoneIds : LiveData<List<String>> get() = _timeZoneIds

    private var _navigateUp = MutableLiveData<Boolean>()
    val navigateUp : LiveData<Boolean> get() = _navigateUp

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {

        getTimeZoneIds()
    }

    private fun getTimeZoneIds(){
        val timeZoneList = mutableListOf<String>()
        for (id in TimeZone.getAvailableIDs()) {

            timeZoneList.add(id)
        }

        _timeZoneIds.value = timeZoneList.sorted()

    }

    fun insert(timeZone: String) {
        val newTimeZone = AttoTimeZone(locale = timeZone, name = timeZone.split("/").last())
        coroutineScope.launch(Dispatchers.IO) {
            repository.insert(newTimeZone)
        }
        _navigateUp.value = true
    }

    fun doneNavigation(){
        _navigateUp.value = false
    }


}
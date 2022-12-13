package com.chihwhsu.atto.timezonepage.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.AttoTimeZone
import com.chihwhsu.atto.data.database.AttoRepository
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TimeZoneDialogViewModel(val repository: AttoRepository) : ViewModel() {

    private var _timeZoneIds = MutableLiveData<List<AttoTimeZone>>()
    val timeZoneIds: LiveData<List<AttoTimeZone>> get() = _timeZoneIds

    private var _navigateUp = MutableLiveData<Boolean>()
    val navigateUp: LiveData<Boolean> get() = _navigateUp

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {

        getTimeZoneIds()
    }

    private fun getTimeZoneIds() {
        val timeZoneList = mutableListOf<AttoTimeZone>()
        for (id in TimeZone.getAvailableIDs()) {

            val timeZone = AttoTimeZone(locale = id, name = id.split("/").last())
            timeZoneList.add(timeZone)
        }

        _timeZoneIds.value = timeZoneList.sortedBy { it.name }
    }

    fun insert(timeZone: AttoTimeZone) {

        coroutineScope.launch(Dispatchers.IO) {
            repository.insert(timeZone)
        }
        _navigateUp.value = true
    }

    fun doneNavigation() {
        _navigateUp.value = false
    }
}

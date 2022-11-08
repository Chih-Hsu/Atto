package com.chihwhsu.atto.clock.alarm

import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AlarmListViewModel(val databaseDao: AttoDatabaseDao) : ViewModel() {

    val alarmList = databaseDao.getTypeEvent(Event.ALARM_TYPE)

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    fun deleteEvent(id : Long){
        coroutineScope.launch(Dispatchers.IO){
            databaseDao.deleteEvent(id)
        }

    }
}
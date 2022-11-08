package com.chihwhsu.atto.clock.todo

import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.data.Event.Companion.TODO_TYPE
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.ext.getTimeFrom00am
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TodoViewModel(val databaseDao: AttoDatabaseDao) : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // for create Event
    private var alarmTime = System.currentTimeMillis()+600000

    private var eventDay = System.currentTimeMillis() - getTimeFrom00am(System.currentTimeMillis())

    private var eventTitle = ""

    private var eventContent = ""


    fun setTitle(title : String){
        eventTitle = title
    }

    fun setContent(content : String){
        eventContent = content
    }

    fun setAlarmTime(time : Long){
        alarmTime = time
    }

    fun setAlarmDay(time: Long){
        eventDay = time
    }

    fun saveEvent(){
//        val totalTime = alarmTime+eventDay
        val newEvent = Event(
            alarmTime = alarmTime + eventDay,
            type = TODO_TYPE,
            title = eventTitle,
            content = eventContent
        )

        coroutineScope.launch(Dispatchers.IO) {
            databaseDao.insert(newEvent)
        }



    }
}


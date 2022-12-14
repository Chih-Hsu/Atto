package com.chihwhsu.atto.clock.todo

import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.data.Event.Companion.TODO_TYPE
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.ext.getTimeFromStartOfDay
import com.chihwhsu.atto.util.MINUTE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: AttoRepository) : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // for create Event
    private var alarmTime = getTimeFromStartOfDay(System.currentTimeMillis()) + 10 * MINUTE

    private var eventDay =
        System.currentTimeMillis() - getTimeFromStartOfDay(System.currentTimeMillis())

    private var eventTitle = ""

    private var eventContent = ""

    fun setTitle(title: String) {
        eventTitle = title
    }

    fun setContent(content: String) {
        eventContent = content
    }

    fun setAlarmTime(time: Long) {
        alarmTime = time
    }

    fun setAlarmDay(time: Long) {
        eventDay = time
    }

    fun saveEvent() {

        val newEvent = Event(
            alarmTime = alarmTime + eventDay,
            type = TODO_TYPE,
            title = eventTitle,
            content = eventContent
        )

        coroutineScope.launch(Dispatchers.IO) {
            repository.insert(newEvent)
        }
    }
}

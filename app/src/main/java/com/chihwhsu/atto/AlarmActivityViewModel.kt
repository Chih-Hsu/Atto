package com.chihwhsu.atto

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.data.database.AttoRepository
import kotlinx.coroutines.*

class AlarmActivityViewModel(val repository: AttoRepository) : ViewModel() {

    private var _event = MutableLiveData<Event>()
    val event : LiveData<Event> get() = _event

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var eventId : Int? = null

    private var _currentIntent = MutableLiveData<Intent>()
    val currentIntent : LiveData<Intent> get() = _currentIntent

    fun getEvent(id : Int){
        eventId = id
        coroutineScope.launch(Dispatchers.Default) {
            val newEvent = repository.getEvent(id)
            withContext(Dispatchers.Main){
                newEvent?.let {
                    _event.value = it
                }

            }
        }
    }

    fun lockApp(label : String){
        if (event.value!!.lockApp == true) {
            if (label == "全部") {
                coroutineScope.launch(Dispatchers.IO) {
                    repository.lockAllApp()
                }
            } else {
                coroutineScope.launch(Dispatchers.IO) {
                    repository.lockSpecificLabelApp(label)
                }
            }
        }
    }

    fun unLockApp(label : String){
        if (label == "全部") {
            coroutineScope.launch(Dispatchers.IO) {
                repository.unLockAllApp()
            }
        }else {
            coroutineScope.launch(Dispatchers.IO) {
                repository.unLockSpecificLabelApp(label)
            }
        }
    }

    fun setIntent(newIntent:Intent){
        _currentIntent.value = newIntent
    }
}
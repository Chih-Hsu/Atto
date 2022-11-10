package com.chihwhsu.atto.homepage


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.data.database.AttoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AttoRepository) : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _event = MutableLiveData<Event>()
    val event : LiveData<Event> get() = _event

    private var _showCard = MutableLiveData<Boolean>().also { it.value = false }
    val showCard : LiveData<Boolean> get() = _showCard

    private var _closeCard = MutableLiveData<Boolean>()
    val closeCard : LiveData<Boolean> get() = _closeCard

    private var _navigateToEdit = MutableLiveData<Boolean>().also {
        it.value = false
    }
    val navigateToEdit : LiveData<Boolean> get() = _navigateToEdit

    val eventList = repository.getAllEvents()

    init {



    }



    fun setEvent(clickEvent: Event){
        _event.value = clickEvent
        // if showCard is true then don't set value again
        if (showCard.value == false) {
            _showCard.value = true
        }
    }

    fun deleteEvent(eventList : List<Event>){
        coroutineScope.launch(Dispatchers.IO) {
            for (event in eventList) {
                repository.deleteEvent(event.id)
            }
        }
    }

    fun delayEvent(event : Event){
        coroutineScope.launch(Dispatchers.IO) {
            val currentEvent = repository.getEvent(event.id)
            val newTime = currentEvent.alarmTime + 5*60*1000
            repository.delayEvent5Minutes(event.id)
        }
    }

    fun initAnimation(){
        _showCard.value = false
    }

    fun beginCloseCard(){
        _closeCard.value = true
        _showCard.value = false
    }

    fun navigateToEdit(){
        if (closeCard.value == false) {
            _navigateToEdit.value = false
        }else{
            _navigateToEdit.value = !navigateToEdit.value!!
        }
    }
}
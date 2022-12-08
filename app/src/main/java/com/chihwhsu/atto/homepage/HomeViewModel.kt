package com.chihwhsu.atto.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.data.database.AttoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AttoRepository) : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _event = MutableLiveData<Event>()
    val event: LiveData<Event> get() = _event

    val timeZoneList = repository.getAllTimeZone()

    private var _showCard = MutableLiveData<Boolean>().also { it.value = false }
    val showCard: LiveData<Boolean> get() = _showCard

    private var _closeCard = MutableLiveData<Boolean>()
    val closeCard: LiveData<Boolean> get() = _closeCard

    private var _showEdit = MutableLiveData<Boolean>().also {
        it.value = false
    }
    val showEdit: LiveData<Boolean> get() = _showEdit

    val eventList = repository.getAllEvents()

    fun setEvent(clickEvent: Event) {
        _event.value = clickEvent
        // if showCard is true then don't set value again
        if (showCard.value == false) {
            _showCard.value = true
        }
    }

    fun deleteEvent(eventList: List<Event>) {
        coroutineScope.launch(Dispatchers.IO) {
            for (event in eventList) {
                repository.deleteEvent(event.id)
            }
        }
    }

    fun delayEvent(event: Event) {
        coroutineScope.launch(Dispatchers.IO) {
            repository.delayEvent5Minutes(event.id)
        }
    }

    fun initAnimation() {
        _showCard.value = false
    }

    fun beginCloseCard() {
        _closeCard.value = true
        _showCard.value = false
    }

    fun navigateToEdit() {
        if (closeCard.value == false) {
            _showEdit.value = false
        } else {
            showEdit.value?.let {
                _showEdit.value = !it
            }
        }
    }
}

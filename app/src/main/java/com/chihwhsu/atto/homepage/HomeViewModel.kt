package com.chihwhsu.atto.homepage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.Event

class HomeViewModel : ViewModel() {

    private var _eventList = MutableLiveData<List<Event>>()
    val eventList : LiveData<List<Event>> get() = _eventList

    private var _event = MutableLiveData<Event>()
    val event : LiveData<Event> get() = _event

    private var _showCard = MutableLiveData<Boolean>().also { it.value = false }
    val showCard : LiveData<Boolean> get() = _showCard

    private var _closeCard = MutableLiveData<Boolean>()
    val closeCard : LiveData<Boolean> get() = _closeCard

    private var _navigateToEdit = MutableLiveData<Event>()
    val navigateToEdit : LiveData<Event> get() = _navigateToEdit

    init {
        val list = mutableListOf<Event>(
            Event(1, true),
            Event(2, true),
            Event(3, false),
            Event(4, false),
            Event(5, false),
            Event(6, false),
            Event(7, false),
            Event(8, false),
            Event(9, false),
            Event(10, false),
            Event(11, false),
            Event(12, false),
            Event(13, false),
            Event(14, false),
            Event(15, false),
            Event(16, false),
            Event(17, false),
            Event(18, false)
        )
        _eventList.value = list
    }

    fun setEvent(clickEvent: Event){
        _event.value = clickEvent
        Log.d("showCard","event value = ${event.value}")
        // if showCard is true then don't set value again
        if (showCard.value == false) {
            _showCard.value = true
            Log.d("showCard","value = ${showCard.value}")
        }
    }

    fun initAnimation(){
        _showCard.value = false
    }

    fun beginCloseCard(){
        Log.d("gesture","viewModel work")
        _closeCard.value = true
        _showCard.value = false
        Log.d("showCard","value = ${showCard.value}")
    }

    fun navigateToEdit(){
        _navigateToEdit.value = event.value
    }
}
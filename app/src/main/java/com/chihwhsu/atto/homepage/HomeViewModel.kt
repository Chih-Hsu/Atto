package com.chihwhsu.atto.homepage


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.data.database.AttoDatabaseDao

class HomeViewModel(val databaseDao: AttoDatabaseDao) : ViewModel() {

//    private var _eventList = MutableLiveData<List<Event>>()
//    val eventList : LiveData<List<Event>> get() = _eventList

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

    val eventList = databaseDao.getAllEvents()

    init {



    }



    fun setEvent(clickEvent: Event){
        _event.value = clickEvent
        // if showCard is true then don't set value again
        if (showCard.value == false) {
            _showCard.value = true
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
package com.chihwhsu.atto.usagelimit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.database.AttoDatabaseDao

class UsageLimitViewModel(val databaseDao: AttoDatabaseDao) : ViewModel() {

    private var _newHour = MutableLiveData<Int>()
    val newHour: LiveData<Int> get() = _newHour

    private var _newMinutes = MutableLiveData<Int>()
    val newMinutes: LiveData<Int> get() = _newMinutes

    init {
        _newHour.value = 0
        _newMinutes.value = 0
    }


    fun addHour(currentHour: Int) {
       _newHour.value =  if (currentHour < 1){
            1
        } else if (currentHour in 1..23){
           (currentHour + 1)
        } else {
            24
        }
    }

    fun addMinutes(currentMinutes: Int) {
        if (currentMinutes in 0..54){
            _newMinutes.value =  currentMinutes + 5
        } else if (currentMinutes == 55){
            _newHour.value = _newHour.value?.plus(1)
            _newMinutes.value = 0
        }
    }

    fun subHour(currentHour: Int) {
        _newHour.value =  if (currentHour in 1..23){
            currentHour - 1
        } else {
            0
        }
    }

    fun subMinutes(currentMinutes: Int) {
        if (currentMinutes in 5..55){
            _newMinutes.value =  currentMinutes - 5
        } else if (currentMinutes <= 5 && _newHour.value!! > 0){
            _newHour.value = _newHour.value?.minus(1)
            _newMinutes.value = 55
        } else{
            _newMinutes.value = 0
        }

    }

    fun lockApp(app:App){
        val hour = newHour.value ?: 0
        val minutes = newMinutes.value ?: 0
        app.lock(databaseDao,hour,minutes)
    }

}
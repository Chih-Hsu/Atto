package com.chihwhsu.atto.clock.alarm

import android.content.Context
import android.media.RingtoneManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.RingTone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AlarmViewModel : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _dayList = MutableLiveData<List<Boolean>>()
    val dayList : LiveData<List<Boolean>> get() = _dayList

    val list = mutableListOf(false,false,false,false,false,false,false)

    val ringToneList = mutableListOf<String>()

    init {
        _dayList.value = list
    }

    fun setWeekRepeat(position :Int){
        list[position] = !list[position]
        _dayList.value = list
    }

    fun getRingTone(context: Context){
        coroutineScope.launch(Dispatchers.Default) {
        val ringToneManager = RingtoneManager(context)
        val cursor = ringToneManager.cursor
        val count = cursor.count

        var number =0
        val listRingTone = mutableListOf<RingTone>()
        while (number<count){
            number += 1
            val ringtoneName = ringToneManager.getRingtone(number).getTitle(context)
            val uri = ringToneManager.getRingtoneUri(number)
            uri?.let {
                val newRingTone = RingTone(ringtoneName,uri)
                listRingTone.add(newRingTone)
            }
        }
        for (item in listRingTone){
            ringToneList.add(item.name)
        }
    }
    }
}
package com.chihwhsu.atto.clock.alarm

import android.content.Context
import android.media.RingtoneManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.data.Event.Companion.ALARM_TYPE
import com.chihwhsu.atto.data.RingTone
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.util.MINUTE
import kotlinx.coroutines.*

class AlarmViewModel(private val repository: AttoRepository) : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _dayList = MutableLiveData<List<Boolean>>()
    val dayList: LiveData<List<Boolean>> get() = _dayList

    private var _ringToneList = MutableLiveData<List<String>>()
    val ringToneList: LiveData<List<String>> get() = _ringToneList

    private var _event = MutableLiveData<Event>()
    val event: LiveData<Event> get() = _event

    private var _navigateToAlarmList = MutableLiveData<Boolean>().also {
        it.value = false
    }
    val navigateToAlarmList: LiveData<Boolean> get() = _navigateToAlarmList

    // for create Event
    private var alarmTime = System.currentTimeMillis() + (10 * MINUTE)

    // current time + 10 minutes
    private var selectRingTonePosition: Int = 0
    private val routineList = mutableListOf(false, false, false, false, false, false, false)
    private var needVibration = false
    private var needSnooze = false

    private val ringtoneList = mutableListOf<RingTone>()

    init {
        _dayList.value = routineList
    }

    fun setWeekRepeat(position: Int) {
        routineList[position] = !routineList[position]
        _dayList.value = routineList
    }

    fun getRingTone(context: Context) {
        coroutineScope.launch(Dispatchers.Default) {
            val ringToneManager = RingtoneManager(context)
            val cursor = ringToneManager.cursor
            val count = cursor.count

            var number = 0

            while (number < count) {
                number += 1
                val ringtoneName = ringToneManager.getRingtone(number).getTitle(context)
                val uri = ringToneManager.getRingtoneUri(number)
                uri?.let {
                    val newRingTone = RingTone(ringtoneName, uri)
                    ringtoneList.add(newRingTone)
                }
            }
            val stringList = mutableListOf<String>()
            for (item in ringtoneList) {
                stringList.add(item.name)
            }
            withContext(Dispatchers.Main) {
                _ringToneList.value = stringList
            }
        }
    }

    fun setAlarmTime(time: Long) {
        alarmTime = time
    }

    fun setRingTonePosition(ringTonePosition: Int) {
        selectRingTonePosition = ringTonePosition
    }

    fun setVibration() {
        needVibration = !needVibration
    }

    fun setSnooze() {
        needSnooze = !needSnooze
    }

    fun saveEvent() {
        val ringTone = ringtoneList[selectRingTonePosition]
        val newEvent = Event(
            alarmTime = alarmTime,
            alarmSoundName = ringTone.name,
            alarmSoundUri = ringTone.path,
            routine = routineList,
            vibration = needVibration,
            snoozeMode = needSnooze,
            type = ALARM_TYPE
        )

        _event.value = newEvent
        coroutineScope.launch(Dispatchers.IO) {
            repository.insert(newEvent)
        }

        _navigateToAlarmList.value = true
    }

    fun doneNavigation() {
        _navigateToAlarmList.value = false
    }
}

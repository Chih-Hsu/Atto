package com.chihwhsu.atto.clock.pomodoro

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.clock.ClockFragment
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.ext.getCurrentDay
import com.chihwhsu.atto.ext.getTimeFrom00am
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PomodoroViewModel(val databaseDao: AttoDatabaseDao) : ViewModel() {


    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val labelList = databaseDao.getLabelList()


    // for create Event
    private var beginTime = getTimeFrom00am(System.currentTimeMillis() + 600000)
    private var workTime: Long = 45 * 60 * 1000
    private var breakTime: Long = 15 * 60 * 1000
    private var routineRound = 1
    private var lockAppMode = false
    private var lockAppLabel = ""


    fun setBeginTime(time: Long) {
        beginTime = time
    }

    fun setWorkTime(time: Long) {
        workTime = time * 60 * 1000
    }

    fun setBreakTime(time: Long) {
        breakTime = time * 60 * 1000
    }

    fun setRoutineRound(routine: Int) {
        routineRound = routine
    }

    fun setLockAppMode() {
        lockAppMode = !lockAppMode
    }

    fun setLockAppLabel(label: String) {
        lockAppLabel = label
    }

    fun saveEvent() {


        for (count in 0 until routineRound) {
            val workStartTime = beginTime + (workTime + breakTime) * count
            val workEndBreakStart = beginTime + workTime + (breakTime + workTime) * count
            val breakEndTime = beginTime + (workTime + breakTime) * (count + 1)

            val workEvent = Event(
                startTime = workStartTime,
                alarmTime = workEndBreakStart,
                alarmDay = getCurrentDay(),
                type = ClockFragment.POMODORO_TYPE,
                lockApp = lockAppMode,
                lockAppLabel = lockAppLabel
            )

            val breakEvent = Event(
                startTime = workEndBreakStart,
                alarmTime = breakEndTime,
                alarmDay = getCurrentDay(),
                type = ClockFragment.POMODORO_TYPE,
                lockApp = lockAppMode,
                lockAppLabel = lockAppLabel
            )

            coroutineScope.launch(Dispatchers.IO) {
                databaseDao.insert(workEvent)
                databaseDao.insert(breakEvent)
            }

        }


    }
}
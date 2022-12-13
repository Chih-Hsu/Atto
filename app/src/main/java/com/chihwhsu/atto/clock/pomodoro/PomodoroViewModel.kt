package com.chihwhsu.atto.clock.pomodoro

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.Event
import com.chihwhsu.atto.data.Event.Companion.POMODORO_BREAK_TYPE
import com.chihwhsu.atto.data.Event.Companion.POMODORO_WORK_TYPE
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.util.MINUTE
import kotlinx.coroutines.*

class PomodoroViewModel(private val repository: AttoRepository) : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    val labelList = repository.getLabelList()

    private var isOtherPomodoroInRoom = false

    // for create Event
    private var beginTime: Long = System.currentTimeMillis() + 10 * MINUTE
    private var workTime: Long = 45 * MINUTE
    private var breakTime: Long = 15 * MINUTE
    private var routineRound = 1
    private var lockAppMode = false
    private var lockAppLabel = ""

    fun setBeginTime(time: Long) {
        beginTime = time
    }

    fun setWorkTime(time: Long) {
        workTime = time * MINUTE
    }

    fun setBreakTime(time: Long) {
        breakTime = time * MINUTE
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

    fun checkCanCreate(): Boolean {
        checkPomodoroInRoom()
        return isOtherPomodoroInRoom
    }

    fun saveEvent(context: Context) {

        for (count in 0 until routineRound) {
            val workStartTime = beginTime + (workTime + breakTime) * count
            val workEndBreakStart = beginTime + workTime + (breakTime + workTime) * count
            val breakEndTime = beginTime + (workTime + breakTime) * (count + 1)

            val workEvent = Event(
                startTime = workStartTime,
                alarmTime = workEndBreakStart,
                type = POMODORO_WORK_TYPE,
                lockApp = lockAppMode,
                lockAppLabel = lockAppLabel
            )

            val breakEvent = Event(
                startTime = workEndBreakStart,
                alarmTime = breakEndTime,
                type = POMODORO_BREAK_TYPE,
                lockApp = lockAppMode,
                lockAppLabel = lockAppLabel
            )

            val workDuration = workEvent.alarmTime - workEvent.startTime!!
            val breakDuration = breakEvent.alarmTime - breakEvent.startTime!!

            workEvent.setPomodoroAlarmTime(context, workEvent.id, workDuration)
            breakEvent.setPomodoroAlarmTime(
                context,
                breakEvent.id,
                breakDuration
            )

            coroutineScope.launch(Dispatchers.IO) {
                repository.insert(workEvent)
                repository.insert(breakEvent)
            }
        }

        _navigateToHome.value = true
    }

    private fun checkPomodoroInRoom() {
        coroutineScope.launch(Dispatchers.Default) {
            isOtherPomodoroInRoom = repository.isPomodoroIsExist()
        }
    }

    fun doneNavigation() {
        _navigateToHome.value = false
    }
}

package com.chihwhsu.atto.clock.pomodoro

import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.database.AttoDatabaseDao

class PomodoroViewModel(val databaseDao: AttoDatabaseDao) : ViewModel() {

    val labelList = databaseDao.getLabelList()
}
package com.chihwhsu.atto.appdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.Theme
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import kotlinx.coroutines.*
import java.time.LocalDate


class AppDetailViewModel(private val databaseDao: AttoDatabaseDao, private val argument: App) :
    ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _app = MutableLiveData<App>().apply {
        value = argument
    }
    val app: LiveData<App> get() = _app

    private var _dataPerHourList = MutableLiveData<List<Float>>()
    val dataPerHourList: LiveData<List<Float>> get() = _dataPerHourList

    private var _weekUsageList = MutableLiveData<List<Float>>()
    val weekUsageList: LiveData<List<Float>> get() = _weekUsageList

    private var _barSet = MutableLiveData<List<Pair<String, Float>>>()
    val barSet: LiveData<List<Pair<String, Float>>> get() = _barSet

    private var _weekBarSet = MutableLiveData<List<Pair<String, Float>>>()
    val weekBarSet: LiveData<List<Pair<String, Float>>> get() = _weekBarSet

    private var _navigateUp = MutableLiveData<Boolean>().also {
        it.value = false
    }

    val navigateUp: LiveData<Boolean> get() = _navigateUp

    init {

    }

    fun setPerHourList(list: List<Float>) {
        _dataPerHourList.value = list
    }

    fun setWeekList(list: List<Float>) {
        _weekUsageList.value = list
    }

    fun create24HBarSet(list: List<Float>) {

        val perHourBarSet = listOf(
            "00" to list[0],
            "." to list[1],
            "." to list[2],
            "." to list[3],
            "04" to list[4],
            "." to list[5],
            "." to list[6],
            "." to list[7],
            "08" to list[8],
            "." to list[9],
            "." to list[10],
            "." to list[11],
            "12" to list[12],
            "." to list[13],
            "." to list[14],
            "." to list[15],
            "16" to list[16],
            "." to list[17],
            "." to list[18],
            "." to list[19],
            "20" to list[20],
            "." to list[21],
            "." to list[22],
            "." to list[23],
            "24" to list[23],
        )
        _barSet.value = perHourBarSet
    }


    fun createWeekChartSet(list: List<Float>) {

        val localDate = LocalDate.now()


        val dayList = listOf(
            localDate.dayOfWeek.name.substring(0..2),
            localDate.minusDays(1).dayOfWeek.name.substring(0..2),
            localDate.minusDays(2).dayOfWeek.name.substring(0..2),
            localDate.minusDays(3).dayOfWeek.name.substring(0..2),
            localDate.minusDays(4).dayOfWeek.name.substring(0..2),
            localDate.minusDays(5).dayOfWeek.name.substring(0..2),
            localDate.minusDays(6).dayOfWeek.name.substring(0..2),

        )

        val weekBarList = listOf(
            dayList[6] to list[0],
            dayList[5] to list[1],
            dayList[4] to list[2],
            dayList[3] to list[3],
            dayList[2] to list[4],
            dayList[1] to list[5],
            dayList[0] to list[6]
        )

        _weekBarSet.value = weekBarList
    }

    fun updateTheme(theme: Theme) {

        coroutineScope.launch(Dispatchers.IO) {
            app.value?.let {
                if (theme.index != it.theme) {
                    databaseDao.updateTheme(it.appLabel, theme.index)
                }
                withContext(Dispatchers.Main) {
                    _navigateUp.value = true
                }
            }
        }
    }

    fun doneNavigation() {
        _navigateUp.value = false
    }
}
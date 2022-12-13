package com.chihwhsu.atto.timezonepage

import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.AttoTimeZone
import com.chihwhsu.atto.data.database.AttoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TimeZoneViewModel(val repository: AttoRepository) : ViewModel() {

    val timeZoneList = repository.getAllTimeZone()

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun remove(attoTimeZone: AttoTimeZone) {
        coroutineScope.launch(Dispatchers.IO) {
            repository.deleteTimeZone(attoTimeZone.id)
        }
    }
}

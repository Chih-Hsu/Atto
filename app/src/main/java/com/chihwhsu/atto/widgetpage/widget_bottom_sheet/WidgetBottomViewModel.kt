package com.chihwhsu.atto.widgetpage.widget_bottom_sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.Widget
import com.chihwhsu.atto.data.database.AttoRepository
import kotlinx.coroutines.*

class WidgetBottomViewModel(val repository: AttoRepository) : ViewModel() {

    private var _navigateUp = MutableLiveData<Boolean>()
    val navigateUp: LiveData<Boolean> get() = _navigateUp

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun saveWidget(widgetLabel: String) {
        coroutineScope.launch(Dispatchers.Default) {
            val newWidget = Widget(label = widgetLabel)
            repository.insert(newWidget)

            withContext(Dispatchers.Main) {
                _navigateUp.value = true
            }
        }
    }
}

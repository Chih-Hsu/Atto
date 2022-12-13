package com.chihwhsu.atto.widgetpage.remove_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.Widget
import com.chihwhsu.atto.data.database.AttoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WidgetRemoveViewModel(val repository: AttoRepository) : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    fun removeWidget(widget: Widget) {
        coroutineScope.launch(Dispatchers.Default) {
            repository.deleteWidget(widget.id)
        }
        _navigateToHome.value = true
    }

    fun doneNavigation() {
        _navigateToHome.value = false
    }
}

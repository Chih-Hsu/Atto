package com.chihwhsu.atto.widgetpage

import android.util.Log
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.Widget
import com.chihwhsu.atto.data.database.AttoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WidgetViewModel(val repository: AttoRepository) : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    val widgets = repository.getAllWidget()


    val catchWidget = mutableMapOf<String, Boolean>()

    fun setCatchWidget(widget: Widget) {

            catchWidget.put(widget.label, true)

    }

    fun checkWidgetVisible(widgetLabel: String): Boolean {
        Log.d("widget","$catchWidget")
        return !catchWidget.containsKey(widgetLabel)
    }

    fun deleteWidget(id:Long){
        coroutineScope.launch(Dispatchers.Default) {
              repository.deleteWidget(id)
        }
    }
}
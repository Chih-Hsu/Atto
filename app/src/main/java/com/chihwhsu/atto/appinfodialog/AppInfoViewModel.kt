package com.chihwhsu.atto.appinfodialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.database.AttoRepository
import kotlinx.coroutines.*

class AppInfoViewModel(private val repository: AttoRepository) : ViewModel(){

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _navigateUp = MutableLiveData<Boolean>()
    val navigateUp : LiveData<Boolean> get() = _navigateUp

    fun removeApp(app: App) {

        coroutineScope.launch(Dispatchers.IO) {
            repository.delete(app.packageName)

            withContext(Dispatchers.Main){
                _navigateUp.value = true
            }
        }
    }

    fun doneNavigation(){
        _navigateUp.value = false
    }
}
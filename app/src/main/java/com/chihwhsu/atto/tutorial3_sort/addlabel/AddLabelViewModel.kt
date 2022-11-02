package com.chihwhsu.atto.tutorial3_sort.addlabel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import kotlinx.coroutines.*

class AddLabelViewModel(val databaseDao: AttoDatabaseDao) : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _navigateToSort = MutableLiveData<Boolean>()
    val navigateToSort : LiveData<Boolean> get() = _navigateToSort

    val noLabelAppList = databaseDao.getNoLabelApps()

    val remainList = mutableListOf<App>()

    fun addToList(app:App){
        if (!remainList.contains(app)){
     remainList.add(app)
        }else{
            remainList.remove(app)
        }
    }

    fun updateAppLabel(label : String){
        coroutineScope.launch(Dispatchers.IO){
            for (app in remainList) {
                databaseDao.updateLabel(app.appLabel,label)
                databaseDao.updateSort(app.appLabel,remainList.indexOf(app))
            }
            withContext(Dispatchers.Main){
                _navigateToSort.value = true
            }
        }
    }

    fun doneNavigation(){
        _navigateToSort.value = false
    }
}
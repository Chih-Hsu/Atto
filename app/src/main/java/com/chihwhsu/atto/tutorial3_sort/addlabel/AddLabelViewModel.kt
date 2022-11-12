package com.chihwhsu.atto.tutorial3_sort.addlabel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.data.database.AttoRepository
import kotlinx.coroutines.*
import java.util.*

class AddLabelViewModel(private val repository: AttoRepository) : ViewModel() {

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _navigateToSort = MutableLiveData<Boolean>()
    val navigateToSort : LiveData<Boolean> get() = _navigateToSort

    private var _filterList = MutableLiveData<List<App>>()
    val filterList : LiveData<List<App>> get() = _filterList

    val noLabelAppList = repository.getNoLabelApps()

    val remainList = mutableListOf<App>()

    // for edit
    private var editLabel :String? = null

    private val originalList = mutableListOf<App>()

    fun addToList(app:App){
//        if (!remainList.contains(app)){
        if (remainList.filter { it.appLabel == app.appLabel }.isEmpty()){
     remainList.add(app)
        }else{
            remainList.remove(app)
        }
    }

    fun setEditLabel(label: String){
        editLabel = label
    }

    fun getData(){
        noLabelAppList.value?.let {
            _filterList.value = it
            originalList.addAll(it)
        }
    }

    fun updateAppLabel(label : String){
        coroutineScope.launch(Dispatchers.IO){
            for (app in remainList) {
                repository.updateLabel(app.appLabel,label)
                repository.updateSort(app.appLabel,remainList.indexOf(app))
            }
            withContext(Dispatchers.Main){
                _navigateToSort.value = true
            }
        }
    }

    fun doneNavigation(){
        _navigateToSort.value = false
    }

    fun filterList(text: String?) {
        if (!text.isNullOrEmpty()) {
            val list = mutableListOf<App>()
            originalList.let {
                for (item in it) {
                    if (item.appLabel.lowercase(Locale.ROOT)
                            .contains(text.lowercase(Locale.ROOT))
                    ) {
                        list.add(item)
                    }
                }
            }
            _filterList.value = list
        } else {
            _filterList.value = originalList
        }
    }
}
package com.chihwhsu.atto.setting.sort.addlabel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.App
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

    val appList = repository.getAllApps()

    private var _labelAppList = MutableLiveData<List<App>>()
    val labelAppList : LiveData<List<App>> get() = _labelAppList

    val remainList = mutableListOf<App>()

    // for edit
    private var editLabel :String? = null

    private val originalList = mutableListOf<App>()

    fun setRemainList(list: List<App>?){
        list?.let {
           remainList.addAll(it)
        }

    }
    fun addToList(app:App){

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

       val newList =  appList.value?.filter { it.label == editLabel || it.label == null  }

        newList?.let {
            _filterList.value = it
            originalList.addAll(it)
        }

        _labelAppList.value = originalList.filter { it.label?.lowercase() == editLabel?.lowercase() }
    }

    fun updateAppLabel(label : String){

//        coroutineScope.launch(Dispatchers.Default) {
            val oldLabelList = originalList.filter{it.label == editLabel }

            if (oldLabelList.size > remainList.size){

                for (app in oldLabelList){

                        if (remainList.filter { it.appLabel == app.appLabel }.isEmpty()) {
                            // if true means the app already removed from the remainList,so remove it's label and sort
                            coroutineScope.launch(Dispatchers.IO) {
                                repository.updateLabel(app.appLabel, null)
                                repository.updateSort(app.appLabel, -1)
                            }
                        }
                    }

            for (app in remainList) {

                coroutineScope.launch(Dispatchers.IO) {
                    repository.updateLabel(app.appLabel, label.lowercase())
                    repository.updateSort(app.appLabel, remainList.indexOf(app))
                }
            }
                _navigateToSort.value = true
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
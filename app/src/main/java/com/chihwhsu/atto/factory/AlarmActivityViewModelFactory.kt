package com.chihwhsu.atto.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.AlarmActivityViewModel
import com.chihwhsu.atto.data.database.AttoRepository

class AlarmActivityViewModelFactory(val repository: AttoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmActivityViewModel::class.java)){
            return AlarmActivityViewModel(repository) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
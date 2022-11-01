package com.chihwhsu.atto.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.main.MainViewModel
import com.chihwhsu.atto.tutorial2_dock.DockViewModel

class MainViewModelFactory(val databaseDao: AttoDatabaseDao) :ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(databaseDao) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
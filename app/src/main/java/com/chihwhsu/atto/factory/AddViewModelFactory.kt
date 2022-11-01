package com.chihwhsu.atto.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.tutorial3_sort.addlabel.AddLabelViewModel

class AddViewModelFactory(val databaseDao: AttoDatabaseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddLabelViewModel::class.java)){
            return AddLabelViewModel(databaseDao) as T
        }else{
            throw IllegalArgumentException("UnKnown ViewModel Class ")
        }
    }
}
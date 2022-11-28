package com.chihwhsu.atto.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.appdetail.AppDetailViewModel
import com.chihwhsu.atto.data.App
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.data.database.local.AttoDatabaseDao

class AppDetailViewModelFactory(val repository: AttoRepository, val argument : App) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppDetailViewModel::class.java)){
            return AppDetailViewModel(repository,argument) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}
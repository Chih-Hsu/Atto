package com.chihwhsu.atto.main

import androidx.lifecycle.ViewModel
import com.chihwhsu.atto.data.database.AttoDatabase
import com.chihwhsu.atto.data.database.AttoDatabaseDao

class MainViewModel(val databaseDao: AttoDatabaseDao) : ViewModel() {

    val dockList = databaseDao.getSpecificLabelApps("dock")
}
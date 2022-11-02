package com.chihwhsu.atto.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.applistpage.AppListViewModel
import com.chihwhsu.atto.applistpage.bottomsheet.AppListDialogViewModel
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.main.MainViewModel
import com.chihwhsu.atto.tutorial2_dock.DockViewModel
import com.chihwhsu.atto.tutorial3_sort.SortAdapter
import com.chihwhsu.atto.tutorial3_sort.SortViewModel
import com.chihwhsu.atto.tutorial3_sort.addlabel.AddLabelViewModel

class ViewModelFactory constructor(
    private val databaseDao: AttoDatabaseDao
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(SortViewModel::class.java) ->
                    SortViewModel(databaseDao)

                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(databaseDao)

                isAssignableFrom(AppListViewModel::class.java) ->
                    AppListViewModel(databaseDao)

                isAssignableFrom(AddLabelViewModel::class.java) ->
                    AddLabelViewModel(databaseDao)

                isAssignableFrom(AppListDialogViewModel::class.java) ->
                    AppListDialogViewModel(databaseDao)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
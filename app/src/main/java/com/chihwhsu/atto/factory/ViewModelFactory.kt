package com.chihwhsu.atto.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.applistpage.AppListViewModel
import com.chihwhsu.atto.applistpage.bottomsheet.AppListBottomViewModel
import com.chihwhsu.atto.clock.alarm.AlarmViewModel
import com.chihwhsu.atto.clock.pomodoro.PomodoroViewModel
import com.chihwhsu.atto.clock.todo.TodoViewModel
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.homepage.HomeViewModel
import com.chihwhsu.atto.main.MainViewModel
import com.chihwhsu.atto.tutorial3_sort.SortViewModel
import com.chihwhsu.atto.tutorial3_sort.addlabel.AddLabelViewModel
import com.chihwhsu.atto.usagelimit.UsageLimitViewModel

class ViewModelFactory constructor(
    private val databaseDao: AttoDatabaseDao
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {

                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(databaseDao)

                isAssignableFrom(SortViewModel::class.java) ->
                    SortViewModel(databaseDao)

                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(databaseDao)

                isAssignableFrom(AppListViewModel::class.java) ->
                    AppListViewModel(databaseDao)

                isAssignableFrom(AddLabelViewModel::class.java) ->
                    AddLabelViewModel(databaseDao)

                isAssignableFrom(AppListBottomViewModel::class.java) ->
                    AppListBottomViewModel(databaseDao)

                isAssignableFrom(UsageLimitViewModel::class.java) ->
                    UsageLimitViewModel(databaseDao)

                isAssignableFrom(PomodoroViewModel::class.java) ->
                    PomodoroViewModel(databaseDao)

                isAssignableFrom(AlarmViewModel::class.java) ->
                    AlarmViewModel(databaseDao)

                isAssignableFrom(TodoViewModel::class.java) ->
                    TodoViewModel(databaseDao)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
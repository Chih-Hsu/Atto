package com.chihwhsu.atto.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.applistpage.AppListViewModel
import com.chihwhsu.atto.applistpage.bottomsheet.AppListBottomViewModel
import com.chihwhsu.atto.clock.alarm.AlarmListViewModel
import com.chihwhsu.atto.clock.alarm.AlarmViewModel
import com.chihwhsu.atto.clock.pomodoro.PomodoroViewModel
import com.chihwhsu.atto.clock.todo.TodoViewModel
import com.chihwhsu.atto.data.database.AttoDatabaseDao
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.homepage.HomeViewModel
import com.chihwhsu.atto.main.MainViewModel
import com.chihwhsu.atto.tutorial3_sort.SortViewModel
import com.chihwhsu.atto.tutorial3_sort.addlabel.AddLabelViewModel
import com.chihwhsu.atto.usagelimit.UsageLimitViewModel
import com.chihwhsu.atto.widgetpage.WidgetViewModel
import com.chihwhsu.atto.widgetpage.widget_bottom_sheet.WidgetBottomViewModel

class ViewModelFactory constructor(
    private val repository: AttoRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {

                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(repository)

                isAssignableFrom(SortViewModel::class.java) ->
                    SortViewModel(repository)

                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(repository)

                isAssignableFrom(AppListViewModel::class.java) ->
                    AppListViewModel(repository)

                isAssignableFrom(AddLabelViewModel::class.java) ->
                    AddLabelViewModel(repository)

                isAssignableFrom(AppListBottomViewModel::class.java) ->
                    AppListBottomViewModel(repository)

                isAssignableFrom(UsageLimitViewModel::class.java) ->
                    UsageLimitViewModel(repository)

                isAssignableFrom(PomodoroViewModel::class.java) ->
                    PomodoroViewModel(repository)

                isAssignableFrom(AlarmViewModel::class.java) ->
                    AlarmViewModel(repository)

                isAssignableFrom(TodoViewModel::class.java) ->
                    TodoViewModel(repository)

                isAssignableFrom(AlarmListViewModel::class.java) ->
                    AlarmListViewModel(repository)

                isAssignableFrom(WidgetBottomViewModel::class.java) ->
                    WidgetBottomViewModel(repository)

                isAssignableFrom(WidgetViewModel::class.java) ->
                    WidgetViewModel(repository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}
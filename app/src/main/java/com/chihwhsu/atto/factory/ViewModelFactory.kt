package com.chihwhsu.atto.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chihwhsu.atto.appinfodialog.AppInfoViewModel
import com.chihwhsu.atto.applistpage.AppListViewModel
import com.chihwhsu.atto.applistpage.bottomsheet.AppListBottomViewModel
import com.chihwhsu.atto.clock.alarm.AlarmListViewModel
import com.chihwhsu.atto.clock.alarm.AlarmViewModel
import com.chihwhsu.atto.clock.pomodoro.PomodoroViewModel
import com.chihwhsu.atto.clock.todo.TodoViewModel
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.homepage.HomeViewModel
import com.chihwhsu.atto.login.LoginViewModel
import com.chihwhsu.atto.main.MainViewModel
import com.chihwhsu.atto.setting.SettingViewModel
import com.chihwhsu.atto.setting.sort.SortViewModel
import com.chihwhsu.atto.setting.sort.addlabel.AddLabelViewModel
import com.chihwhsu.atto.syncpage.SyncViewModel
import com.chihwhsu.atto.timezonepage.TimeZoneViewModel
import com.chihwhsu.atto.timezonepage.dialog.TimeZoneDialogViewModel
import com.chihwhsu.atto.usagelimit.UsageLimitViewModel
import com.chihwhsu.atto.widgetpage.WidgetViewModel
import com.chihwhsu.atto.widgetpage.remove_dialog.WidgetRemoveViewModel
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

                isAssignableFrom(WidgetRemoveViewModel::class.java) ->
                    WidgetRemoveViewModel(repository)

                isAssignableFrom(SyncViewModel::class.java) ->
                    SyncViewModel(repository)

                isAssignableFrom(AppInfoViewModel::class.java) ->
                    AppInfoViewModel(repository)

                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(repository)

                isAssignableFrom(TimeZoneViewModel::class.java) ->
                    TimeZoneViewModel(repository)

                isAssignableFrom(TimeZoneDialogViewModel::class.java) ->
                    TimeZoneDialogViewModel(repository)

                isAssignableFrom(SettingViewModel::class.java) ->
                    SettingViewModel(repository)

                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}

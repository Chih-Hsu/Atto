package com.chihwhsu.atto

import android.app.Application
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.util.ServiceLocator
import kotlin.properties.Delegates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class AttoApplication : Application() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val attoRepository: AttoRepository
        get() = ServiceLocator.provideTasksRepository(this, coroutineScope)

    companion object {
        var instance: AttoApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}

package com.chihwhsu.atto

import android.app.Application
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.util.ServiceLocator
import kotlin.properties.Delegates

class AttoApplication : Application() {


    val attoRepository: AttoRepository
        get() = ServiceLocator.provideTasksRepository(this)

    companion object {
        var instance: AttoApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
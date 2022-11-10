package com.chihwhsu.atto.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.chihwhsu.atto.data.database.AttoDataSource
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.data.database.DefaultAttoRepository
import com.chihwhsu.atto.data.database.local.AttoLocalDataSource
import com.chihwhsu.atto.data.database.remote.AttoRemoteDataSource
import com.chihwhsu.atto.data.database.system.AttoSystemDataSource

object ServiceLocator {

    @Volatile
    var attoRepository: AttoRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(context: Context): AttoRepository {
        synchronized(this) {
            return attoRepository
                ?: attoRepository
                ?: createStylishRepository(context)
        }
    }

    private fun createStylishRepository(context: Context): AttoRepository {
        return DefaultAttoRepository(
            AttoRemoteDataSource,
            createLocalDataSource(context),
            createSystemDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): AttoDataSource {
        return AttoLocalDataSource(context)
    }

    private fun createSystemDataSource(context: Context): AttoDataSource {
        return AttoSystemDataSource(context)
    }
}
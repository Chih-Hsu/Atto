package com.chihwhsu.atto.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.chihwhsu.atto.data.database.AttoDataSource
import com.chihwhsu.atto.data.database.AttoRepository
import com.chihwhsu.atto.data.database.DefaultAttoRepository
import com.chihwhsu.atto.data.database.local.AttoLocalDataSource
import com.chihwhsu.atto.data.database.remote.AttoRemoteDataSource
import com.chihwhsu.atto.data.database.system.AttoSystemDataSource
import kotlinx.coroutines.CoroutineScope

object ServiceLocator {

    @Volatile
    var attoRepository: AttoRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(context: Context,currentCoroutine: CoroutineScope): AttoRepository {
        synchronized(this) {
            return attoRepository
                ?: attoRepository
                ?: createStylishRepository(context,currentCoroutine)
        }
    }

    private fun createStylishRepository(context: Context,currentCoroutine: CoroutineScope): AttoRepository {
        return DefaultAttoRepository(
            AttoRemoteDataSource,
            createLocalDataSource(context),
            createSystemDataSource(context,currentCoroutine)
        )
    }

    private fun createLocalDataSource(context: Context): AttoDataSource {
        return AttoLocalDataSource(context)
    }

    private fun createSystemDataSource(context: Context,currentCoroutine: CoroutineScope): AttoDataSource {
        return AttoSystemDataSource(context,currentCoroutine)
    }
}
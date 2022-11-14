package com.chihwhsu.atto.background

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.chihwhsu.atto.data.database.AttoDatabase
import java.util.*
import java.util.concurrent.TimeUnit

class ResetWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams){

    override fun doWork(): Result {

        resetTimer()
        setResetWorker(applicationContext)

        return Result.success()
    }

    private fun resetTimer(){
        val databaseDao = AttoDatabase.getInstance(applicationContext).attoDatabaseDao
        databaseDao.deleteAllTimer()
        databaseDao.unLockAllApp(true,false)
    }

    fun setResetWorker(context: Context){
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        // Set new OneTime worker
        dueDate.set(Calendar.HOUR_OF_DAY, 0)
        dueDate.set(Calendar.MINUTE, 0)
        dueDate.set(Calendar.SECOND, 0)

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        val dailyWorkRequest = OneTimeWorkRequestBuilder<ResetWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag("reset worker")
            .build()

        WorkManager.getInstance(context).enqueue(dailyWorkRequest)
    }
}
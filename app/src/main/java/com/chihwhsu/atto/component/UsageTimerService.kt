package com.chihwhsu.atto.component

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.chihwhsu.atto.MainActivity
import com.chihwhsu.atto.R
import kotlinx.coroutines.*
import java.util.*

class UsageTimerService : Service() {

    val CHANNEL_ID = "channelId"
    val CHANNEL_NAME = "foreground_service"
    val NOTIFICATION_ID = 1

    val scope = CoroutineScope(Dispatchers.Main)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val time = intent?.getIntExtra("limitTime",0)?.div(1000)

        Log.d("limit","$time")

        createNotificationChannel()
        val notificationIntent = Intent(this@UsageTimerService, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this@UsageTimerService,
            0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )

            scope.launch {
                createNotificationChannel()
                for (i in 0..time!!){
                    delay(1000)

                    Log.d("limit","${time-i}")

                    val notification: Notification = NotificationCompat.Builder(this@UsageTimerService, CHANNEL_ID)
                        .setContentTitle("Atto Usage Limit Countdown")
                        .setContentText("剩餘 ${time -i} 秒")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentIntent(pendingIntent)
                        .build()


                    startForeground(NOTIFICATION_ID,notification)
            }
        }

        return START_STICKY

    }


    private fun notificationToCountDown(time : Long) : Notification {
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Simple Foreground Service")
            .setContentText("Explain about the service")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        return notification
    }

    fun createNotificationChannel(){
        val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT).apply {
            lightColor = Color.GREEN
            enableLights(true)
        }
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }




}
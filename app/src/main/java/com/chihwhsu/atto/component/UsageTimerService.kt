package com.chihwhsu.atto.component

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.chihwhsu.atto.AlarmActivity
import com.chihwhsu.atto.AttoApplication
import com.chihwhsu.atto.MainActivity
import com.chihwhsu.atto.R
import com.chihwhsu.atto.ext.toMinuteSecondFormat
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

        val duration = intent?.getLongExtra("duration",0L)

        createNotificationChannel()
        duration?.let {
            createTimer(it)
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

    fun createTimer(duration : Long){
        val countDownTimer =
            object : CountDownTimer(duration, 1000L) {
                override fun onTick(millisUntilFinished: Long) {

                    val notificationIntent = Intent(this@UsageTimerService, AlarmActivity::class.java)
                    val pendingIntent = PendingIntent.getActivity(
                        this@UsageTimerService,
                        0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
                    )

                    val notification: Notification = NotificationCompat.Builder(this@UsageTimerService, CHANNEL_ID)
                        .setContentTitle("Atto Usage Limit Countdown")
                        .setContentText("剩餘時間  ${duration.toMinuteSecondFormat()} ")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentIntent(pendingIntent)
                        .build()

                    startForeground(NOTIFICATION_ID,notification)

                }

                override fun onFinish() {
                    val intent = Intent(AttoApplication.instance.applicationContext, UsageTimerService::class.java)
                    stopService(intent)
                }
            }
        countDownTimer.start()
    }




}
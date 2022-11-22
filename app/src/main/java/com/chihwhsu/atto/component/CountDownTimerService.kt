package com.chihwhsu.atto.component

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.chihwhsu.atto.AlarmActivity
import com.chihwhsu.atto.AttoApplication
import com.chihwhsu.atto.MainActivity
import com.chihwhsu.atto.R
import com.chihwhsu.atto.ext.toMinuteSecondFormat
import kotlinx.coroutines.*

class CountDownTimerService : Service() {

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


    private fun createNotificationChannel(){
        val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    private fun createTimer(duration : Long){
        val countDownTimer =
            object : CountDownTimer(duration, 1000L) {
                override fun onTick(millisUntilFinished: Long) {

                    val notificationIntent = Intent(this@CountDownTimerService, AlarmActivity::class.java)
                    val pendingIntent = PendingIntent.getActivity(
                        this@CountDownTimerService,
                        0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
                    )

                    val notification: Notification = NotificationCompat.Builder(this@CountDownTimerService, CHANNEL_ID)
                        .setContentTitle("Atto Usage Limit Countdown")
                        .setContentText("剩餘時間  ${millisUntilFinished.toMinuteSecondFormat()} ")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentIntent(pendingIntent)
                        .build()

                    startForeground(NOTIFICATION_ID,notification)
                }

                override fun onFinish() {
                    val intent = Intent(AttoApplication.instance.applicationContext, CountDownTimerService::class.java)
                    stopService(intent)
                }
            }
        countDownTimer.start()
    }




}
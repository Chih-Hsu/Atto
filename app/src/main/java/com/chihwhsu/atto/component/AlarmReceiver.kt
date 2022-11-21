package com.chihwhsu.atto.component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chihwhsu.atto.AlarmActivity
import com.chihwhsu.atto.util.AlarmManagerUtil

class AlarmReceiver : BroadcastReceiver() {



    override fun onReceive(context: Context?, intent: Intent?) {

        val ringTone = intent!!.getStringExtra("ringTone")
        val intervalMillis = intent.getLongExtra("intervalMillis", 0)
        val duration = intent.getLongExtra("duration",0L)
        if (intervalMillis != 0L) {
            AlarmManagerUtil.setAlarmTime(
                context!!, System.currentTimeMillis() + intervalMillis,
                intent
            )
        }

        val flag = intent.getIntExtra("soundOrVibrator", 0)
        val id = intent.getIntExtra("id",0)
        val clockIntent = Intent(context, AlarmActivity::class.java)
        clockIntent.putExtra("ringTone", ringTone)
        clockIntent.putExtra("flag", flag)
        clockIntent.putExtra("id",id)
        clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)


        context?.let {
            val serviceIntent = Intent(it,UsageTimerService::class.java)
            serviceIntent.putExtra("duration",duration)
            it.startService(serviceIntent)
            it.startActivity(clockIntent)
        }

    }
}
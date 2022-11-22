package com.chihwhsu.atto.component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chihwhsu.atto.AlarmActivity
import com.chihwhsu.atto.util.AlarmManagerUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    val scope = CoroutineScope(Dispatchers.Main)

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

        context?.let { thisContext ->
            scope.launch {
                val serviceIntent = Intent(thisContext,CountDownTimerService::class.java)
                serviceIntent.putExtra("duration",duration)
                thisContext.startService(serviceIntent)
            }
            thisContext.startActivity(clockIntent)
        }

    }
}
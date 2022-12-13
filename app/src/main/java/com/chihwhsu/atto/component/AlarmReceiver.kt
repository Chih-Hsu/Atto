package com.chihwhsu.atto.component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chihwhsu.atto.AlarmActivity
import com.chihwhsu.atto.util.AlarmManagerUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AlarmReceiver : BroadcastReceiver() {

    val scope = CoroutineScope(Dispatchers.Main)

    override fun onReceive(context: Context?, intent: Intent?) {

        val ringTone = intent!!.getStringExtra(RINGTONE)
        val intervalMillis = intent.getLongExtra(INTERVAL_MILLIS, 0)
        if (intervalMillis != 0L) {
            AlarmManagerUtil.setAlarmTime(
                context!!, System.currentTimeMillis() + intervalMillis,
                intent
            )
        }

        val flag = intent.getIntExtra(SOUND_OR_VIBRATOR, 0)
        val id = intent.getIntExtra(ID, 0)
        val clockIntent = Intent(context, AlarmActivity::class.java)
        clockIntent.putExtra(RINGTONE, ringTone)
        clockIntent.putExtra(FLAG, flag)
        clockIntent.putExtra(ID, id)
        clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context?.startActivity(clockIntent)
    }

    companion object {
        private const val RINGTONE = "ringTone"
        private const val FLAG = "flag"
        private const val ID = "id"
        private const val SOUND_OR_VIBRATOR = "soundOrVibrator"
        private const val INTERVAL_MILLIS = "intervalMillis"

    }
}

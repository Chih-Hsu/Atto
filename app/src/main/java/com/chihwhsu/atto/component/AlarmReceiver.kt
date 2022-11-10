package com.chihwhsu.atto.component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Vibrator
import android.util.Log
import com.chihwhsu.atto.MainActivity
import com.chihwhsu.atto.clock.alarm.AlarmFragment
import com.chihwhsu.atto.util.AlarmManagerUtil

class AlarmReceiver : BroadcastReceiver() {


    ///
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator
///

    override fun onReceive(context: Context?, intent: Intent?) {

        val msg = intent!!.getStringExtra("msg")
        val intervalMillis = intent.getLongExtra("intervalMillis", 0)
        if (intervalMillis != 0L) {
            AlarmManagerUtil.setAlarmTime(
                context!!, System.currentTimeMillis() + intervalMillis,
                intent
            )
        }

        Log.d("clocktest","Work")
        val flag = intent.getIntExtra("soundOrVibrator", 0)
        val clockIntent = Intent(context, MainActivity::class.java)
        clockIntent.putExtra("msg", msg)
        clockIntent.putExtra("flag", flag)
        clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context!!.startActivity(clockIntent)
    }








//    override fun onReceive(context: Context?, intent: Intent) {
//
//        if (intent.getBooleanExtra("Alarm", false)) {
//            context?.let {
//                // Only vibrate in foreground
//                val vibrator = it.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//                vibrator.vibrate(5000)
//                val ringTone = RingtoneManager
//                    .getRingtone(
//                        it,
//                        intent.getParcelableExtra<Uri>("Ringtone")
//                    )
//
//                ringTone.play()
//
//
//            }
//        } else {
//
//
//        }
//    }
}
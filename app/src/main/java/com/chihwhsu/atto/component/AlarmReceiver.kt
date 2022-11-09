package com.chihwhsu.atto.component

import android.app.AlertDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.core.app.DialogCompat

class AlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent) {

        if (intent.getBooleanExtra("Alarm", false)) {
            context?.let {
                // Only vibrate in foreground
                val vibrator = it.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(5000)
                val ringTone = RingtoneManager
                    .getRingtone(
                        it,
                        intent.getParcelableExtra<Uri>("Ringtone")
                    )

                ringTone.play()


            }
        } else {


        }
    }
}
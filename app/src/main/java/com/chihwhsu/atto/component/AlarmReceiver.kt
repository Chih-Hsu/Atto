package com.chihwhsu.atto.component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Vibrator
import android.util.Log
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {



    override fun onReceive(context: Context?, intent: Intent) {
        context?.let {
            val vibrator = it.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(2000)

            val ringTone = RingtoneManager
                .getRingtone(it, Uri.parse("content://media/internal/audio/media/202?title=Aquila&canonical=1)"))

            ringTone.play()

        }


        Log.d("clock","succeed")
            Toast.makeText(context, "hello alarm", Toast.LENGTH_LONG).show()
            println("hello alarm")

    }
}
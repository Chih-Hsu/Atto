package com.chihwhsu.atto.widgetpage

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chihwhsu.atto.component.AlarmReceiver
import com.chihwhsu.atto.component.UsageTimerService
import com.chihwhsu.atto.databinding.FragmentWidgetBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class WidgetFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWidgetBinding.inflate(inflater,container,false)



       binding.textView2.setOnClickListener {
//           setAlarmTime(requireContext(),1000)
//           startUsageService()
           Log.d("calendar","${binding.sss.selectedDates}")
       }
        val calen = GregorianCalendar(2022,12,9)
        val calen2 = GregorianCalendar(2022,12,15)
        binding.sss.selectedDates = listOf(calen,calen2)
        binding.sss.isClickable = true
//        binding.sss.setDisabledDays(listOf(calen,calen2))


        val startTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(166666), ZoneId.systemDefault())
        startTime.dayOfWeek


        return binding.root
    }

    private fun setAlarmTime(context: Context, triggerAtMillis:Long){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(),AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(requireActivity().applicationContext,0,intent, FLAG_IMMUTABLE)
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, sender)
    }
    fun startUsageService(){
        val serviceIntent = Intent(this.context, UsageTimerService::class.java)
        serviceIntent.putExtra("limitTime", 10000)
        requireContext().startService(serviceIntent)
    }


}
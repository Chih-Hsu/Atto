package com.chihwhsu.atto.widgetpage

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.chihwhsu.atto.R
import com.chihwhsu.atto.component.AlarmReceiver
import com.chihwhsu.atto.databinding.FragmentWidgetBinding
import java.util.*


class WidgetFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWidgetBinding.inflate(inflater,container,false)



       binding.textView2.setOnClickListener {
           setAlarmTime(requireContext(),1000)
       }

        val events: MutableList<EventDay> = ArrayList()

        val calendar = Calendar.getInstance()
        calendar.set(2022, 10, 2);
        val newCalendar = Calendar.getInstance()
        newCalendar.set(2022,10,9)
        events.add(EventDay(calendar, R.drawable.sample))
        events.add(EventDay(newCalendar, R.drawable.sample))


//        calendarView.setDate(calendar);
        binding.calendar.setHighlightedDays(listOf(calendar,newCalendar))
//        binding.calendar == CalendarView.MANY_DAYS_PICKER
//        binding.calendar.selectedDates = listOf(calendar,newCalendar)
//        binding.calendar.selectedDates.addAll(listOf(calendar,newCalendar))
//        binding.calendar.setEvents(events)



        return binding.root
    }

    private fun setAlarmTime(context: Context, triggerAtMillis:Long){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(),AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(requireActivity().applicationContext,0,intent, FLAG_IMMUTABLE)
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, sender)
    }


}
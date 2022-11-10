package com.chihwhsu.atto.util

import android.app.usage.EventStats
import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import com.chihwhsu.atto.ext.getTimeFrom00am
import com.chihwhsu.atto.ext.toFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime


object UsageStatesManager {


    fun getTodayUsage(context: Context, packageName: String): Long {

        val usageStateManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val start = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val now = ZonedDateTime.now().toInstant().toEpochMilli()

        // from 0:00 to now
//        val states = usageStateManager.queryAndAggregateUsageStats(start, now)

        val states = usageStateManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, start, now
        )
        var usageTime =-1L

        for (state in states) {
            val name = state.packageName
            val totalTime = state.totalTimeInForeground

            if (name == packageName) {
                usageTime += totalTime
            }
        }

        return usageTime

    }

    fun getTotalUsage(context: Context, packageName: String): Long {

        val usageStateManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
//        val start = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val start = LocalDate.now().withDayOfYear(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val now = ZonedDateTime.now().toInstant().toEpochMilli()

        // from 0:00 to now
//        val states = usageStateManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,0,now)
        val states = usageStateManager.queryUsageStats(
            UsageStatsManager.INTERVAL_YEARLY, start, now
        )
        var usageTime =-1L

        for (state in states) {
            val name = state.packageName
            val totalTime = state.totalTimeInForeground
            if (name == packageName) {
                usageTime = totalTime
            }

        }
//        LocalDate.now().


        return usageTime

    }

    // Not Complete
    fun getWeekUsageList(context: Context, packageName: String) {
        val usageStateManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val start = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
            .toEpochMilli() - 6 * 86400000

        val now = ZonedDateTime.now().toInstant().toEpochMilli()


        val states = usageStateManager.queryAndAggregateUsageStats(start, now)
        var usageTime = -1L

        val value = states.get("com.chihwhsu.atto")?.totalTimeInForeground

    }

    fun get24hrUsageList(context: Context, packageName: String): MutableList<Float> {
        val usageStateManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        // get
        val start = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val now = ZonedDateTime.now().hour + 1

        val list = mutableListOf<Float>()


        for (count in 0 until now) {

            val usageEvents = usageStateManager.queryEvents(
                start + count * 1000 * 60 * 60,
                start + (count + 1) * 1000 * 60 * 60
            )
            val currentHourEventList = mutableListOf<Pair<Int, Long>>()

            var currentHourUsage = 0L
            while (usageEvents.hasNextEvent()) {
                val currentEvent = UsageEvents.Event()
                usageEvents.getNextEvent(currentEvent)
                if (currentEvent.packageName == packageName) {
                    if (currentEvent.eventType == 1 || currentEvent.eventType == 2 || currentEvent.eventType == 23) {
                        val newMap = Pair<Int, Long>(currentEvent.eventType, currentEvent.timeStamp)
                        currentHourEventList.add(newMap)

                    }
                }
            }

            if (currentHourEventList.size != 0) {
                for (position in 0 until currentHourEventList.size - 1) {

                    if (currentHourEventList.get(position).first == UsageEvents.Event.ACTIVITY_RESUMED) {

                        if (currentHourEventList.get(position + 1).first == UsageEvents.Event.ACTIVITY_PAUSED || currentHourEventList.get(
                                position + 1
                            ).first == UsageEvents.Event.ACTIVITY_STOPPED
                        ) {
                            val startTime = currentHourEventList.get(position).second
                            val endTime = currentHourEventList.get(position + 1).second
                            currentHourUsage += (endTime - startTime)
                        }
                    }
                }
            }


            val hours = currentHourUsage.toFloat() / (60000).toFloat()
            list.add(hours)

        }

        if (list.size <24){
            for (i in list.size until 24){
                list.add(0f)
            }
        }

        return list


    }

    fun getUsageFromStartTime( context : Context, packageName : String ,startTime : Long ) : Long{
        val usageStateManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val now = System.currentTimeMillis()

        val list = mutableListOf<Float>()


            val usageEvents = usageStateManager.queryEvents(startTime, now)

            val currentHourEventList = mutableListOf<Pair<Int, Long>>()
            var currentHourUsage = 0L
            while (usageEvents.hasNextEvent()) {
                val currentEvent = UsageEvents.Event()
                usageEvents.getNextEvent(currentEvent)
                if (currentEvent.packageName == packageName) {
                    if (currentEvent.eventType == 1 || currentEvent.eventType == 2 || currentEvent.eventType == 23) {
                        val newMap = Pair<Int, Long>(currentEvent.eventType, currentEvent.timeStamp)
                        currentHourEventList.add(newMap)
                    }
                }
            }

            if (currentHourEventList.size != 0) {
                for (position in 0 until currentHourEventList.size - 1) {

                    if (currentHourEventList.get(position).first == UsageEvents.Event.ACTIVITY_RESUMED) {

                        if (currentHourEventList.get(position + 1).first == UsageEvents.Event.ACTIVITY_PAUSED || currentHourEventList.get(
                                position + 1
                            ).first == UsageEvents.Event.ACTIVITY_STOPPED
                        ) {
                            val start = currentHourEventList.get(position).second
                            val end = currentHourEventList.get(position + 1).second
                            currentHourUsage += (end - start)
                        }
                    }
                }
            }


        return currentHourUsage


    }


}
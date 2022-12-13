package com.chihwhsu.atto.util

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

object UsageStatesManager {

    fun getTodayUsage(context: Context, packageName: String): Long {

        val usageStateManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val start = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val now = ZonedDateTime.now().toInstant().toEpochMilli()

        val states = usageStateManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, start, now
        )
        var usageTime = 0L

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
        val start =
            LocalDate.now().withDayOfYear(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
                .toEpochMilli()
        val now = ZonedDateTime.now().toInstant().toEpochMilli()

        // Get this year data
        val states = usageStateManager.queryUsageStats(
            UsageStatsManager.INTERVAL_YEARLY, start, now
        )

        var usageTime = 0L

        for (state in states) {
            val name = state.packageName
            val totalTime = state.totalTimeInForeground
            if (name == packageName) {
                usageTime = totalTime
            }
        }

        return usageTime
    }

    fun getWeekUsageList(context: Context, packageName: String): MutableList<Float> {

        val usageStateManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        // Start form six day ago , End in end of today
        val start = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
            .toEpochMilli() - 6 * DAY

        val list = mutableListOf<Float>()

        for (count in 0 until 6) {
            var currentDayUsage = 0L
            val usageEvents = usageStateManager.queryEvents(
                start + count * DAY,
                start + (count + 1) * DAY
            )

            val weekEventList = mutableListOf<Pair<Int, Long>>()

            while (usageEvents.hasNextEvent()) {
                // Filter correct event in all events, and add to weekEventList
                getExistEvents(usageEvents, packageName, weekEventList)
            }

            if (weekEventList.isNotEmpty()) {
                // add all current day event
                currentDayUsage = calculateTime(weekEventList)
            }

            val day = currentDayUsage.toFloat() / MINUTE.toFloat()
            list.add(day)
        }

        // if data size is less 7 , add to 7
        if (list.size < 7) {
            for (i in list.size until 7) {
                list.add(0f)
            }
        }

        return list
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
                start + count * HOUR,
                start + (count + 1) * HOUR
            )

            val currentHourEventList = mutableListOf<Pair<Int, Long>>()
            while (usageEvents.hasNextEvent()) {

                getExistEvents(usageEvents, packageName, currentHourEventList)
            }

            var currentHourUsage = 0L
            if (currentHourEventList.size != 0) {
                currentHourUsage = calculateTime(currentHourEventList)
            }

            val hours = currentHourUsage.toFloat() / MINUTE.toFloat()
            list.add(hours)
        }

        if (list.size < 24) {
            for (i in list.size until 24) {
                list.add(0f)
            }
        }

        return list
    }

    fun getUsageFromStartTime(context: Context, packageName: String, startTime: Long): Long {

        val usageStateManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val now = System.currentTimeMillis()
        val usageEvents = usageStateManager.queryEvents(startTime, now)

        // For calculate
        val currentHourEventList = mutableListOf<Pair<Int, Long>>()
        var currentHourUsage = 0L

        // If have event , do calculate
        while (usageEvents.hasNextEvent()) {

            getExistEvents(usageEvents, packageName, currentHourEventList)
        }

        if (currentHourEventList.size != 0) {
            for (position in 0 until currentHourEventList.size - 1) {
                currentHourUsage = calculateTime(currentHourEventList)
            }
        }

        return currentHourUsage
    }

    private fun calculateTime(
        eventList: MutableList<Pair<Int, Long>>
    ): Long {
        var total = 0L
        for (position in 0 until eventList.size - 1) {
            val nextPosition = position + 1

            if (eventList[position].first == UsageEvents.Event.ACTIVITY_RESUMED &&
                (eventList[nextPosition].first == UsageEvents.Event.ACTIVITY_PAUSED ||
                        eventList[nextPosition].first == UsageEvents.Event.ACTIVITY_STOPPED
                        )
            ) {
                val startTime = eventList[position].second
                val endTime = eventList[nextPosition].second
                total += (endTime - startTime)
            }
        }
        return total
    }

    private fun getExistEvents(
        usageEvents: UsageEvents,
        packageName: String,
        weekEventList: MutableList<Pair<Int, Long>>
    ) {
        val currentEvent = UsageEvents.Event()
        usageEvents.getNextEvent(currentEvent)

        if (isCurrentPackageName(currentEvent.packageName,packageName) &&
            (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED ||   // Get Screen Time Event
                    currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED ||   // Get Pause Event
                    currentEvent.eventType == UsageEvents.Event.ACTIVITY_STOPPED     // Get Stop Event
                    )
        ) {

            val newMap = Pair(currentEvent.eventType, currentEvent.timeStamp)

            // Save Event in List
            weekEventList.add(newMap)
        }
    }

    private fun isCurrentPackageName(currentPackageName:String, packageName: String):Boolean{
        return currentPackageName == packageName

    }

    private const val SECOND = 1000L
    private const val MINUTE = SECOND * 60L
    private const val HOUR = MINUTE * 60L
    private const val DAY = MINUTE * 24L
}

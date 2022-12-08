package com.chihwhsu.atto.util

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
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
//        val start = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val start =
            LocalDate.now().withDayOfYear(1).atStartOfDay(ZoneId.systemDefault()).toInstant()
                .toEpochMilli()
        val now = ZonedDateTime.now().toInstant().toEpochMilli()

        // from 0:00 to now
//        val states = usageStateManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,0,now)
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

        val start = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
            .toEpochMilli() - 6 * DAY

        val now = ZonedDateTime.now().toInstant().toEpochMilli()

        val list = mutableListOf<Float>()
        var currentDayUsage = 0L

        for (count in 0 until 6) {

            val usageEvents = usageStateManager.queryEvents(
                start + count * DAY,
                start + (count + 1) * DAY
            )

            val weekEventList = mutableListOf<Pair<Int, Long>>()

            while (usageEvents.hasNextEvent()) {

                val currentEvent = UsageEvents.Event()
                usageEvents.getNextEvent(currentEvent)

                if (currentEvent.packageName == packageName) {

                    if (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED
                        || currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED || currentEvent.eventType == UsageEvents.Event.ACTIVITY_STOPPED) {

                        val newMap = Pair(currentEvent.eventType, currentEvent.timeStamp)

                        weekEventList.add(newMap)
                    }
                }
            }

            if (weekEventList.size != 0) {
                for (position in 0 until weekEventList.size - 1) {

                    if (weekEventList[position].first == UsageEvents.Event.ACTIVITY_RESUMED) {
                        if (weekEventList[position + 1].first == UsageEvents.Event.ACTIVITY_PAUSED || weekEventList.get(
                                position + 1
                            ).first == UsageEvents.Event.ACTIVITY_STOPPED
                        ) {
                            val startTime = weekEventList.get(position).second
                            val endTime = weekEventList.get(position + 1).second
                            currentDayUsage += (endTime - startTime)
                        }
                    }
                }
            }

            val day = currentDayUsage.toFloat() / MINUTE.toFloat()
            list.add(day)
            Log.d("usage", "$currentDayUsage")
        }

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

            var currentHourUsage = 0L

            while (usageEvents.hasNextEvent()) {

                val currentEvent = UsageEvents.Event()
                usageEvents.getNextEvent(currentEvent)

                if (currentEvent.packageName == packageName) {
                    if (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED || currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED || currentEvent.eventType == UsageEvents.Event.ACTIVITY_STOPPED) {
                        val newMap = Pair(currentEvent.eventType, currentEvent.timeStamp)
                        currentHourEventList.add(newMap)
                    }
                }
            }

            if (currentHourEventList.size != 0) {

                for (position in 0 until currentHourEventList.size - 1) {

                    if (currentHourEventList[position].first == UsageEvents.Event.ACTIVITY_RESUMED) {

                        if (currentHourEventList[position + 1].first == UsageEvents.Event.ACTIVITY_PAUSED || currentHourEventList.get(
                                position + 1
                            ).first == UsageEvents.Event.ACTIVITY_STOPPED
                        ) {
                            val startTime = currentHourEventList[position].second
                            val endTime = currentHourEventList[position + 1].second
                            currentHourUsage += (endTime - startTime)
                        }
                    }
                }
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

        val currentHourEventList = mutableListOf<Pair<Int, Long>>()
        var currentHourUsage = 0L
        while (usageEvents.hasNextEvent()) {
            val currentEvent = UsageEvents.Event()
            usageEvents.getNextEvent(currentEvent)
            if (currentEvent.packageName == packageName) {
                if (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED || currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED || currentEvent.eventType == UsageEvents.Event.ACTIVITY_STOPPED) {
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

    private const val SECOND = 1000L
    private const val MINUTE = SECOND * 60L
    private const val HOUR = MINUTE * 60L
    private const val DAY = MINUTE * 24L
}

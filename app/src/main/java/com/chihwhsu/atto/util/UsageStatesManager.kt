package com.chihwhsu.atto.util

import android.app.usage.UsageStatsManager
import android.content.Context
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

object UsageStatesManager {



    fun getUsage(context: Context,packageName:String):Long {

        val usageStateManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val start = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val now = ZonedDateTime.now().toInstant().toEpochMilli()

        // from 0:00 to now
        val states = usageStateManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, start, now
        )
        var usageTime =-1L

        for (state in states) {
            val name = state.packageName
            val totalTime = state.totalTimeInForeground
            if (name == packageName) {
                usageTime = totalTime
            }

        }

        return usageTime

    }



}
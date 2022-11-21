package com.chihwhsu.atto.component

import android.content.Context
import android.util.AttributeSet
import android.widget.TextClock
import com.chihwhsu.atto.R
import java.util.*


class CustomTextClock : TextClock {


    constructor(context: Context?) : super(context) {
        setLocaleDateFormat()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {

        setLocaleDateFormat()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {

        setLocaleDateFormat()
    }


    private fun setLocaleDateFormat() {
        val currentLocale = Locale.ENGLISH

        val calendar: Calendar = GregorianCalendar.getInstance(TimeZone.getDefault(), currentLocale)
        val monthName: String =
            calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, currentLocale) as String

        val amPm: String =
            calendar.getDisplayName(Calendar.AM_PM, Calendar.LONG, currentLocale) as String

        when (this.id) {
            R.id.clock_month -> {
                this.format12Hour = "'${monthName.uppercase()}' d"
                this.format24Hour = null
            }

            R.id.clock_minutes -> {
                this.format12Hour = "'${amPm.uppercase()}' hh:mm"
                this.format24Hour = null
            }
        }

    }
}
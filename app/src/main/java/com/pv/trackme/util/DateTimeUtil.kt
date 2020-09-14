package com.pv.trackme.util

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {

    fun getNonTimeCalendar(): Calendar {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY))
        cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE))
        cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND))
        cal.set(Calendar.MILLISECOND, cal.getActualMinimum(Calendar.MILLISECOND))
        return cal
    }

    fun formatDateTime(pattern: String, dateTimeInMillis: Long): String {
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return simpleDateFormat.format(dateTimeInMillis)
    }

    fun parseDateTime(pattern: String, dateTimeInString: String): Long {
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return simpleDateFormat.parse(dateTimeInString)!!.time
    }

}
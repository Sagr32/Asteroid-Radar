package com.sagr.asteroidradar.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private var dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun getTodayDate(): String {
        val calendar: Calendar = Calendar.getInstance()

        return dateFormat.format(calendar.time)
    }

    fun getEndDate(): String {
        val calendar: Calendar = Calendar.getInstance()

        calendar.add(Calendar.DAY_OF_YEAR, 7)
        return dateFormat.format(Date(calendar.timeInMillis))
    }


}


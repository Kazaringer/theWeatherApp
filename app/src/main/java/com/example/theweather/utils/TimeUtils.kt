package com.example.theweather.utils

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {
    companion object {
        fun timestampToDateString(timestamp: Long): String {
            val dateFormat = SimpleDateFormat("dd/M/yyyy \n hh:mm:ss")
            return dateFormat.format(timestampToDate(timestamp))
        }

        fun timestampToDate(timestamp: Long): Date {
            return Date(timestamp)
        }
    }
}
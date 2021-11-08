package com.example.theweather.utils

import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {
    companion object {
        private val dateFormat = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

        fun timestampToDateString(timestamp: Long): String {
            return timestampToDateString(timestampToDate(timestamp))
        }

        fun timestampToDateString(date: Date): String {
            return dateFormat.format(date)
        }

        fun timestampToDate(timestamp: Long): Date {
            return Date(timestamp)
        }
    }
}
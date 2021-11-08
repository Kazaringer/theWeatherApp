package com.example.theweather.utils

import android.util.Log

class DebugConsole {

    companion object {
        private val DefaultTag = "TweWeather_DebugConsole"

        fun message(tag: String? = DefaultTag, message: String) {
            Log.d(tag, message)
        }

        fun error(tag: String? = DefaultTag, message: String) {
            Log.d(tag, message)
        }

        fun warning(tag: String? = DefaultTag, message: String) {
            Log.d(tag, message)
        }

        fun assert(tag: String? = DefaultTag, condition: Boolean, message: String) {
            if (!condition)
                Log.d(tag, message)

        }
    }
}
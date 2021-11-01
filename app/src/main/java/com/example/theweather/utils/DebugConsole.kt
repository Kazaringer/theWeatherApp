package com.example.theweather.utils

import android.util.Log

class DebugConsole {
    companion object {
        fun message(instance: Any, message: String) {
            Log.d(instance::class.toString(), message)
        }

        fun error(instance: Any, message: String) {
            Log.e(instance::class.toString(), message)
        }

        fun warning(instance: Any, message: String) {
            Log.w(instance::class.toString(), message)
        }

        fun assert(instance: Any, condition: Boolean, message: String) {
            if (!condition)
                Log.d(instance::class.toString(), message)
        }
    }
}
package com.example.theweather.utils

import com.example.theweather.utils.RequestStatus.ERROR
import com.example.theweather.utils.RequestStatus.SUCCESS
import com.example.theweather.utils.RequestStatus.LOADING

class Resource <out T>(val status: RequestStatus, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(status = SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): Resource<T> =
            Resource(status = ERROR, data = data, message = message)

        fun <T> loading(data: T?): Resource<T> = Resource(status = LOADING, data = data, message = null)
    }
}
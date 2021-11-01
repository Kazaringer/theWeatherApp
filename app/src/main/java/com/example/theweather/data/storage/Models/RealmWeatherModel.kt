package com.example.theweather.data.storage.Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import org.bson.types.ObjectId
import org.jetbrains.annotations.NotNull
import java.util.*

open class RealmWeatherModel constructor(
    @PrimaryKey
    var id: String = ObjectId().toHexString(),
    @Required
    var city: String? = "",
    @Required
    var temperatureFahrenheit: Double? = null,
    @Required
    var temperatureCelsius: Double? = null,
    @Required
    var dateTime: Date? = null
) : RealmObject()
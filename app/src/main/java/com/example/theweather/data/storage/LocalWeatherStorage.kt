package com.example.theweather.data.storage

import com.example.theweather.data.storage.Models.RealmWeatherModel
import com.example.theweather.data.storage.Models.WeatherModel
import com.example.theweather.presentation.di.DBModule
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class LocalWeatherStorage @Inject constructor(
    private val realmFactory: DBModule.RealmInstanceFactory
) :
    WeatherStorage {

    private val realm: Realm
        get() {
            return realmFactory.getRealm()
        }

    override suspend fun get(): List<WeatherModel> {
        val weatherModels = mutableListOf<WeatherModel>()

        realm.executeTransactionAwait(coroutineContext) { realmTransaction ->
            weatherModels.addAll(
                realmTransaction
                    .where(RealmWeatherModel::class.java)
                    .findAll()
                    .map { convertModels(realmWeatherModel = it) })
        }


        return weatherModels
    }

    override suspend fun set(weatherModel: WeatherModel) {


        realm.executeTransactionAwait(coroutineContext) { realmTransaction ->
            realmTransaction.insert(convertModels(weatherModel = weatherModel))
        }
    }

    private fun convertModels(realmWeatherModel: RealmWeatherModel): WeatherModel {
        return WeatherModel(
            city = realmWeatherModel.city ?: "",
            temperatureFahrenheit = realmWeatherModel.temperatureFahrenheit ?: .0,
            temperatureCelsius = realmWeatherModel.temperatureCelsius ?: .0,
            dateTime = realmWeatherModel.dateTime ?: Date()
        )
    }

    private fun convertModels(weatherModel: WeatherModel): RealmWeatherModel {
        return RealmWeatherModel(
            city = weatherModel.city,
            temperatureFahrenheit = weatherModel.temperatureFahrenheit,
            temperatureCelsius = weatherModel.temperatureCelsius,
            dateTime = weatherModel.dateTime
        )
    }

}
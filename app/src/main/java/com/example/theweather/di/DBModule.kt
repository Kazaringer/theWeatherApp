package com.example.theweather.di

import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmMigrationNeededException
import java.lang.Exception
import javax.inject.Inject


@Module
class DBModule {
    private val realmVersion = 1L

    @Provides
    fun providesRealmConfig(): RealmConfiguration =
        RealmConfiguration.Builder()
            .schemaVersion(realmVersion)
            .build()

    class RealmInstanceFactory @Inject constructor(private val configuration: RealmConfiguration) {
        fun getRealm(): Realm {
            return try {
                Realm.getInstance(configuration)
            } catch (e: RealmMigrationNeededException) {
                try {
                    Realm.deleteRealm(configuration)
                    Realm.getInstance(configuration)
                } catch (ex: Exception) {
                    throw ex
                }
            }
        }

    }


}
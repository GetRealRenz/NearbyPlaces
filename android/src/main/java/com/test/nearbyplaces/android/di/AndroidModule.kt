package com.test.nearbyplaces.android.di

import android.content.Context
import androidx.room.Room
import com.test.nearbyplaces.android.database.AndroidDatabase
import com.test.nearbyplaces.android.database.AndroidDatabaseProvider
import com.test.nearbyplaces.android.location.AppLocationProvider
import com.test.nearbyplaces.android.system.AppSystemProvider
import com.test.nearbyplaces.data.datasource.system.DatabaseProvider
import com.test.nearbyplaces.data.datasource.system.LocationProvider
import com.test.nearbyplaces.data.datasource.system.SystemProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AndroidModule {
    @Provides
    @Singleton
    fun provideSystemProvider(@ApplicationContext context: Context): SystemProvider =
        AppSystemProvider(context)

    @Provides
    fun provideLocationProvider(appLocationProvider: AppLocationProvider): LocationProvider =
        appLocationProvider

    @Singleton
    @Provides
    fun provideDatabaseProvider(androidDatabaseProvider: AndroidDatabaseProvider): DatabaseProvider =
        androidDatabaseProvider

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext app: Context): AndroidDatabase =
        Room.databaseBuilder(app, AndroidDatabase::class.java, "app_db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    @Provides
    @Singleton
    fun providePlacesDao(appDatabase: AndroidDatabase) = appDatabase.placesDao()
}
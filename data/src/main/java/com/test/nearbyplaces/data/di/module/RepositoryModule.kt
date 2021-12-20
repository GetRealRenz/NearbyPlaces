package com.test.nearbyplaces.data.di.module

import com.test.nearbyplaces.data.repository.LocationRepositoryImpl
import com.test.nearbyplaces.data.repository.PlacesRepositoryImpl
import com.test.nearbyplaces.domain.repository.LocationRepository
import com.test.nearbyplaces.domain.repository.PlacesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePlacesRepository(placesRepositoryImpl: PlacesRepositoryImpl): PlacesRepository =
        placesRepositoryImpl

    @Provides
    @Singleton
    fun provideLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository =
        locationRepositoryImpl
}
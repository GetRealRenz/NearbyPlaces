package com.test.nearbyplaces.data.repository

import com.test.nearbyplaces.data.datasource.system.LocationProvider
import com.test.nearbyplaces.domain.model.geo.ILocation
import com.test.nearbyplaces.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(private val locationProvider: LocationProvider) :
    LocationRepository {
    override suspend fun getUserLocation(): ILocation {
        return locationProvider.getLastKnownLocation()
    }

    override fun getDistanceBetweenLocations(center: ILocation, northeast: ILocation): Float {
        return locationProvider.distanceBetweenLocations(center, northeast)
    }
}
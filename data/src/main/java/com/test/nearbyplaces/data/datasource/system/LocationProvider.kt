package com.test.nearbyplaces.data.datasource.system

import com.test.nearbyplaces.domain.model.geo.ILocation
import com.test.nearbyplaces.domain.model.geo.LocationRequest
import kotlinx.coroutines.flow.Flow

interface LocationProvider {
    suspend fun getLastKnownLocation(): ILocation
    fun getUpdatedLocation(locationRequest: LocationRequest): Flow<ILocation>
    fun distanceBetweenLocations(center: ILocation, northeast: ILocation): Float
}
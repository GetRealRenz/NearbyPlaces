package com.test.nearbyplaces.domain.repository

import com.test.nearbyplaces.domain.model.geo.ILocation

interface LocationRepository {
    suspend fun getUserLocation(): ILocation
    fun getDistanceBetweenLocations(center: ILocation, northeast: ILocation): Float
}
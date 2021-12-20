package com.test.nearbyplaces.domain.repository

import com.test.nearbyplaces.domain.model.NearbyPlace
import com.test.nearbyplaces.domain.model.geo.ILocation
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {
    suspend fun getNearbyPlaces(from: ILocation, to: ILocation)
    fun subscribeToNearbyPlaces(from: ILocation, to: ILocation): Flow<List<NearbyPlace>>
    fun getPlaceByAddress(address: String): Flow<NearbyPlace?>
}
package com.test.nearbyplaces.data.datasource.system

import com.test.nearbyplaces.data.dto.PlaceDto
import kotlinx.coroutines.flow.Flow

interface DatabaseProvider {
    fun savePlaces(places: List<PlaceDto>)
    fun getPlaces(): Flow<List<PlaceDto>>
    fun deletePlaces()
    fun getPlaceByAddress(address: String): Flow<PlaceDto?>
}
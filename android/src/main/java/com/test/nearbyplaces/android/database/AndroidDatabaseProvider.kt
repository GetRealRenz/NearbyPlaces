package com.test.nearbyplaces.android.database

import com.test.nearbyplaces.android.database.dao.PlacesDao
import com.test.nearbyplaces.android.mappers.PlaceMapper
import com.test.nearbyplaces.data.datasource.system.DatabaseProvider
import com.test.nearbyplaces.data.dto.PlaceDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AndroidDatabaseProvider @Inject constructor(
    private val placesDao: PlacesDao,
    private val placeMapper: PlaceMapper
) : DatabaseProvider {
    override fun savePlaces(places: List<PlaceDto>) {
        placesDao.savePlaces(places.map(placeMapper::fromDto))
    }

    override fun getPlaces(): Flow<List<PlaceDto>> {
        return placesDao.getPlaces()
            .map { places ->
                places.map(placeMapper::toDto)
            }
    }

    override fun deletePlaces() {
        placesDao.deleteAll()
    }

    override fun getPlaceByAddress(address: String): Flow<PlaceDto?> {
        return placesDao.getPlaceByAddress(address).map { place ->
            place?.let {
                placeMapper.toDto(it)
            }
        }
    }
}
package com.test.nearbyplaces.domain.usecase

import com.test.nearbyplaces.domain.model.NearbyPlace
import com.test.nearbyplaces.domain.model.geo.ILocation
import com.test.nearbyplaces.domain.model.geo.Location
import com.test.nearbyplaces.domain.repository.LocationRepository
import com.test.nearbyplaces.domain.repository.PlacesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SubscribeToNearbyPlacesUseCase @Inject constructor(
    private val placesRepository: PlacesRepository,
    private val locationRepository: LocationRepository
) {

    operator fun invoke(from: ILocation, to: ILocation): Flow<List<NearbyPlace>> {
        val mapRadius = locationRepository.getDistanceBetweenLocations(from, to)
        return placesRepository.subscribeToNearbyPlaces(from, to).map { places ->
            places.filter {
                val location = it.location
                val distanceToCenter = locationRepository.getDistanceBetweenLocations(
                    from,
                    Location(location.latitude, location.longitude)
                )
                mapRadius > distanceToCenter
            }
        }
    }
}
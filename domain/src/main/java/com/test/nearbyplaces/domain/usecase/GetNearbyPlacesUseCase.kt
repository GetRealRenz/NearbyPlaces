package com.test.nearbyplaces.domain.usecase

import com.test.nearbyplaces.domain.model.geo.ILocation
import com.test.nearbyplaces.domain.repository.PlacesRepository
import javax.inject.Inject

class GetNearbyPlacesUseCase @Inject constructor(
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(from: ILocation, to: ILocation) {
        placesRepository.getNearbyPlaces(from, to)
    }
}
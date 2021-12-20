package com.test.nearbyplaces.domain.usecase

import com.test.nearbyplaces.domain.repository.LocationRepository
import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor(private val locationRepository: LocationRepository) {
    suspend operator fun invoke() = locationRepository.getUserLocation()
}
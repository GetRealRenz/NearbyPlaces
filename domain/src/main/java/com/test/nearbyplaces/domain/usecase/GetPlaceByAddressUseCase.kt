package com.test.nearbyplaces.domain.usecase

import com.test.nearbyplaces.domain.repository.PlacesRepository
import javax.inject.Inject

class GetPlaceByAddressUseCase @Inject constructor(private val placesRepository: PlacesRepository) {
    operator fun invoke(address: String) = placesRepository.getPlaceByAddress(address)
}
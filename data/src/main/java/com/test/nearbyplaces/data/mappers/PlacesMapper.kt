package com.test.nearbyplaces.data.mappers

import com.esri.arcgisruntime.tasks.geocode.GeocodeResult
import com.test.nearbyplaces.data.dto.PlaceDto
import com.test.nearbyplaces.domain.mappers.ThreeWayMapper
import com.test.nearbyplaces.domain.model.NearbyPlace
import com.test.nearbyplaces.domain.model.geo.Location
import javax.inject.Inject

class PlacesMapper @Inject constructor() : ThreeWayMapper<NearbyPlace, PlaceDto, GeocodeResult> {
    override fun remoteToLocal(type: GeocodeResult): PlaceDto {
        return PlaceDto(
            0,
            type.displayLocation.y,
            type.displayLocation.x,
            type.attributes["PlaceName"].toString(),
            type.attributes["Place_addr"].toString()
        )
    }

    override fun localToDomain(type: PlaceDto): NearbyPlace {
        return NearbyPlace(
            type.id,
            Location(type.latitude, type.longitude),
            type.name,
            type.address
        )
    }

    override fun domainToLocal(type: NearbyPlace): PlaceDto {
        return PlaceDto(0, type.location.latitude, type.location.longitude, type.name, type.address)
    }

    override fun remoteToDomain(type: GeocodeResult): NearbyPlace {
        val displayLocation = type.displayLocation
        return NearbyPlace(
            0,
            Location(
                displayLocation.y,
                displayLocation.x
            ),
            type.attributes["PlaceName"].toString(),
            type.attributes["Place_addr"].toString()
        )
    }
}
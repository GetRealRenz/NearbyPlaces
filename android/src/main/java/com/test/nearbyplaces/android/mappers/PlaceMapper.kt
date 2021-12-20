package com.test.nearbyplaces.android.mappers

import com.test.nearbyplaces.android.database.models.PlaceLocal
import com.test.nearbyplaces.data.dto.PlaceDto
import com.test.nearbyplaces.domain.mappers.Mapper
import javax.inject.Inject

class PlaceMapper @Inject constructor() : Mapper<PlaceDto, PlaceLocal> {
    override fun fromDto(type: PlaceDto): PlaceLocal {
        return PlaceLocal().apply {
            latitude = type.latitude
            longitude = type.longitude
            name = type.name
            address = type.address
        }
    }

    override fun toDto(type: PlaceLocal): PlaceDto {
        return PlaceDto(type.id, type.latitude, type.longitude, type.name, type.address)
    }
}
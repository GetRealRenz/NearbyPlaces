package com.test.nearbyplaces.domain.model

import com.test.nearbyplaces.domain.model.geo.Location

data class NearbyPlace(
    val id:Int,
    val location: Location,
    val name: String,
    val address: String
)

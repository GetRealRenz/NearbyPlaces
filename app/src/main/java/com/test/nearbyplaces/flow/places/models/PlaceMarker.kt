package com.test.nearbyplaces.flow.places.models

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.test.nearbyplaces.domain.model.geo.Location

class PlaceMarker constructor(
    val id:Int,
    val location: Location,
    val name: String,
    val address: String
) : ClusterItem {

    override fun getPosition(): LatLng {
        return LatLng(location.latitude, location.longitude)
    }

    override fun getTitle(): String {
        return name
    }

    override fun getSnippet(): String? {
        return null
    }
}
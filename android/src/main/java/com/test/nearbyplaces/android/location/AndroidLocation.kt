package com.test.nearbyplaces.android.location

import android.location.Location
import com.test.nearbyplaces.domain.model.geo.ILocation

class AndroidLocation(location: Location) : ILocation {
    override var latitude: Double = location.latitude
    override var longitude: Double = location.longitude
}
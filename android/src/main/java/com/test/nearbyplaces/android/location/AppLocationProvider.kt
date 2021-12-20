package com.test.nearbyplaces.android.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.test.nearbyplaces.android.exceptions.GpsUnavailableException
import com.test.nearbyplaces.data.datasource.system.LocationProvider
import com.test.nearbyplaces.data.datasource.system.SystemProvider
import com.test.nearbyplaces.domain.model.geo.ILocation
import com.test.nearbyplaces.domain.model.geo.LocationRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

typealias AndroidLocationRequest = com.google.android.gms.location.LocationRequest

@ExperimentalCoroutinesApi
class AppLocationProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val systemProvider: SystemProvider
) : LocationProvider {
    private val locationProvider = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    @Throws(SecurityException::class)
    override suspend fun getLastKnownLocation(): ILocation {
        val location = locationProvider.lastLocation.await()
        return AndroidLocation(location)
    }

    @SuppressLint("MissingPermission")
    override fun getUpdatedLocation(locationRequest: LocationRequest) = channelFlow<ILocation> {

        val isLocationEnabled = systemProvider.isLocationEnabled()
        if (!isLocationEnabled) {
            throw GpsUnavailableException()
        }
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach {
                    sendCatching(AndroidLocation(it))
                }
            }

        }
        val androidLocationRequest = AndroidLocationRequest()
            .apply {
                priority = locationRequest.priority
                interval = locationRequest.interval
            }
        locationProvider.requestLocationUpdates(androidLocationRequest, locationCallback, null)
            .await()
        awaitClose {
            locationProvider.removeLocationUpdates(locationCallback)
        }
    }

    override fun distanceBetweenLocations(center: ILocation, northeast: ILocation): Float {
        val from =
            Location("")
                .apply {
                    latitude = center.latitude
                    longitude = center.longitude
                }
        val to = Location("").apply {
            latitude = northeast.latitude
            longitude = northeast.longitude
        }
        return from.distanceTo(to)
    }

    fun <E> SendChannel<E>.sendCatching(element: E): Boolean {
        return runCatching { offer(element) }.getOrDefault(false)
    }
}
package com.test.nearbyplaces.android.system

import android.content.Context
import android.location.LocationManager
import com.test.nearbyplaces.data.datasource.system.SystemProvider
import javax.inject.Inject

class AppSystemProvider @Inject constructor(private val context: Context) : SystemProvider {
    override suspend fun isLocationEnabled(): Boolean {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}
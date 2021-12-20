package com.test.nearbyplaces.data.datasource.system

interface SystemProvider {
    suspend fun isLocationEnabled():Boolean
}
package com.test.nearbyplaces.data.repository

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.geometry.Point
import com.esri.arcgisruntime.geometry.SpatialReferences
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters
import com.esri.arcgisruntime.tasks.geocode.LocatorTask
import com.test.nearbyplaces.data.BuildConfig
import com.test.nearbyplaces.data.datasource.system.DatabaseProvider
import com.test.nearbyplaces.data.mappers.PlacesMapper
import com.test.nearbyplaces.data.utils.ArcGisConstants
import com.test.nearbyplaces.domain.model.NearbyPlace
import com.test.nearbyplaces.domain.model.geo.ILocation
import com.test.nearbyplaces.domain.repository.PlacesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class PlacesRepositoryImpl @Inject constructor(
    private val databaseProvider: DatabaseProvider,
    private val mapper: PlacesMapper
) :
    PlacesRepository {
    init {
        ArcGISRuntimeEnvironment.setApiKey(BuildConfig.ARC_GIS_KEY)
    }

    @ExperimentalCoroutinesApi
    override suspend fun getNearbyPlaces(from: ILocation, to: ILocation) {
        val mapper = PlacesMapper()
        val searchParameters = GeocodeParameters()
            .apply {
                categories.add(ArcGisConstants.SEARCH_CATEGORY)
                maxResults = ArcGisConstants.MAX_RESULTS
                preferredSearchLocation =
                    Point(from.longitude, from.latitude, SpatialReferences.getWgs84())
                resultAttributeNames.add("*")
            }
        val locator =
            LocatorTask(BuildConfig.ARC_GIS_URL)

        val suggestFuture = locator.geocodeAsync("", searchParameters)
        val suggestionsCallback: () -> Unit = {
            try {
                val searchResults = suggestFuture.get()
                databaseProvider.deletePlaces()
                databaseProvider.savePlaces(searchResults.map(mapper::remoteToLocal))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        suggestFuture.addDoneListener(suggestionsCallback)
    }

    override fun subscribeToNearbyPlaces(from: ILocation, to: ILocation): Flow<List<NearbyPlace>> {
        return databaseProvider.getPlaces().map { places ->
            places.map(mapper::localToDomain)
        }

    }

    override fun getPlaceByAddress(address: String): Flow<NearbyPlace?> {
        return databaseProvider.getPlaceByAddress(address)
            .map { place ->
                place?.let {
                    mapper.localToDomain(it)
                }
            }
    }
}
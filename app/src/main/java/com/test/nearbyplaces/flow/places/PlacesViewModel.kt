package com.test.nearbyplaces.flow.places

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.test.nearbyplaces.android.location.AndroidLocation
import com.test.nearbyplaces.domain.model.NearbyPlace
import com.test.nearbyplaces.domain.model.geo.ILocation
import com.test.nearbyplaces.domain.usecase.GetNearbyPlacesUseCase
import com.test.nearbyplaces.domain.usecase.GetUserLocationUseCase
import com.test.nearbyplaces.domain.usecase.SubscribeToNearbyPlacesUseCase
import com.test.nearbyplaces.flow.places.models.PlaceMarker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlacesUiState(
    val userLocation: ILocation,
    val mapMarkers: List<PlaceMarker>,
    val cameraLocation: LatLng?,
    val zoom: Float,
) {
    companion object {
        fun empty() = PlacesUiState(
            AndroidLocation(
                Location("")
            ), emptyList(),
            null,
            14f
        )
    }
}

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val getNearbyPlacesUseCase: GetNearbyPlacesUseCase,
    private val getUserLocationUseCase: GetUserLocationUseCase,
    private val subscribeToNearbyPlacesUseCase: SubscribeToNearbyPlacesUseCase
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(
        PlacesUiState.empty()
    )
    val uiState: StateFlow<PlacesUiState> = _uiState

    var locationJob: Job? = null


    fun setLocationForMapPosition(center: LatLng, bounds: LatLng?) = viewModelScope.launch {
        locationJob?.cancel()
        val (from, to) = getMapBounds(center, bounds)
        subscribeToNearbyPlaces(center, bounds)
        getNearbyPlacesUseCase(from, to)

    }


    fun getUserLocation() = viewModelScope.launch {
        val userLocation = getUserLocationUseCase()
        _uiState.emit(_uiState.value.copy(userLocation = userLocation))
    }
    fun setCameraLocation(cameraLocation: LatLng?, zoom: Float) = viewModelScope.launch {
        _uiState.emit(
            _uiState.value.copy(
                cameraLocation = cameraLocation,
                zoom = zoom
            )
        )
    }
    fun subscribeToNearbyPlaces(
        center: LatLng,
        bounds: LatLng?
    ) {
        locationJob?.cancel()
        val (from, to) = getMapBounds(center, bounds)
        locationJob = subscribeToNearbyPlacesUseCase(from, to).onEach {
            val markers = mapToMarkers(it)
            try {
                _uiState.emit(_uiState.value.copy(mapMarkers = markers))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.launchIn(viewModelScope)
    }

    private fun mapToMarkers(nearbyPlaces: List<NearbyPlace>) =
        nearbyPlaces.map {
            PlaceMarker(it.id, it.location, it.name, it.address)
        }

    private fun getMapBounds(
        center: LatLng,
        bounds: LatLng?
    ): Pair<AndroidLocation, AndroidLocation> {
        val from = AndroidLocation(Location("")
            .apply {
                latitude = center.latitude
                longitude = center.longitude
            })
        val to = AndroidLocation(Location("").apply {
            latitude = bounds?.latitude ?: 0.0
            longitude = bounds?.longitude ?: 0.0
        })
        return Pair(from, to)
    }
}
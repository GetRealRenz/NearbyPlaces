package com.test.nearbyplaces.flow.places.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.nearbyplaces.domain.model.NearbyPlace
import com.test.nearbyplaces.domain.usecase.GetPlaceByAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlaceDetailsUiState(val nearbyPlace: NearbyPlace? = null)

@HiltViewModel
class PlaceDetailsViewModel @Inject constructor(private val getPlaceByAddressUseCase: GetPlaceByAddressUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow(
        PlaceDetailsUiState()
    )
    val uiState: StateFlow<PlaceDetailsUiState> = _uiState

    fun getPlaceByAddress(address: String) = viewModelScope.launch {
        getPlaceByAddressUseCase(address).collect {
            _uiState.tryEmit(_uiState.value.copy(it))
        }
    }
}
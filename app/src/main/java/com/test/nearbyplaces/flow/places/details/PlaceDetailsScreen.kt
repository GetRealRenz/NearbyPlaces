package com.test.nearbyplaces.flow.places.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.test.nearbyplaces.domain.model.NearbyPlace

@Composable
fun PlaceDetailsScreen(viewModel: PlaceDetailsViewModel = hiltViewModel(), address: String) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(address) {
        viewModel.getPlaceByAddress(address)
    }
    val nearbyPlace = uiState.nearbyPlace
    Box(
        Modifier.fillMaxSize(0.7f),
        contentAlignment = Alignment.CenterStart
    ) {
        if (nearbyPlace == null) {
            EmptyPlace()
        } else {
            PlaceInfo(nearbyPlace)
        }
    }


}

@Composable
private fun PlaceInfo(nearbyPlace: NearbyPlace) {
    Column() {
        Text(text = nearbyPlace.name)
        Text(text = nearbyPlace.address)
    }
}

@Composable
private fun EmptyPlace() {
    Box(Modifier.fillMaxSize()) {
        Text(text = "No information about this place", Modifier.align(Alignment.Center))
    }
}
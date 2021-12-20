package com.test.nearbyplaces.flow.places.map

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.awaitMap
import com.test.nearbyplaces.flow.places.PlacesUiState
import com.test.nearbyplaces.flow.places.PlacesViewModel
import com.test.nearbyplaces.flow.places.models.PlaceMarker
import com.test.nearbyplaces.navigation.Screen
import com.test.nearbyplaces.utils.rememberMapViewWithLifecycle
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@Composable
fun PlacesMapScreen(viewModel: PlacesViewModel = viewModel(), navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar() {
                Text("Nearby Places")
            }
        }
    ) {
        PlacesMap(viewModel, navController)
    }

}

@ExperimentalPermissionsApi
@Composable
private fun PlacesMap(viewModel: PlacesViewModel, navController: NavHostController) {

    val locationPermissionState =
        rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()
    PermissionRequired(
        permissionState = locationPermissionState,
        permissionNotGrantedContent = {
            if (doNotShowRationale) {
                Text("Feature not available")
            } else {
                Column {
                    Text("Location tracking is important for this app. Please grant the permission.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                            Text("Ok!")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { doNotShowRationale = true }) {
                            Text("Nope")
                        }
                    }
                }
            }
        },
        permissionNotAvailableContent = {
            //We still can use map even without user location tracking
            MapContainer(viewModel, uiState, navController)
        }
    ) {
        MapContainer(viewModel, uiState, navController)

    }
}

@SuppressLint("MissingPermission", "PotentialBehaviorOverride")
@ExperimentalPermissionsApi
@Composable
private fun MapContainer(
    viewModel: PlacesViewModel,
    uiState: PlacesUiState,
    navController: NavHostController
) {
    val coroutineScope = rememberCoroutineScope()
    val map = rememberMapViewWithLifecycle()
    val locationPermissionState =
        rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    val context = LocalContext.current



    LaunchedEffect(false) {
        if (locationPermissionState.hasPermission) {
            viewModel.getUserLocation()
        }
    }
    //Trigger when we have new user location
    UpdateOnLocationChange(uiState, map, viewModel)

    UpdateOnMapChange(
        map,
        locationPermissionState,
        context,
        navController,
        viewModel, uiState
    )

    Box {
        AndroidView({ map }) { mapView ->
        }
        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (locationPermissionState.hasPermission) {
                FloatingActionButton(
                    modifier = Modifier.size(46.dp),
                    onClick = {
                        coroutineScope.launch {
                            val googleMap = map.awaitMap()
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        uiState.userLocation.latitude,
                                        uiState.userLocation.longitude
                                    ), 14f
                                )
                            )
                        }
                    }) {
                    Icon(Icons.Filled.LocationOn, null)
                }
            }

            FloatingActionButton(onClick = { navController.navigate(Screen.List.route) }) {
                Icon(Icons.Filled.List, null)
            }
        }
    }
}

@SuppressLint("PotentialBehaviorOverride", "MissingPermission")
@ExperimentalPermissionsApi
@Composable
private fun UpdateOnMapChange(
    map: MapView,
    locationPermissionState: PermissionState,
    context: Context,
    navController: NavHostController,
    viewModel: PlacesViewModel,
    uiState: PlacesUiState
) {
    var clusterManager: ClusterManager<PlaceMarker>? by remember {
        mutableStateOf(null)
    }
    LaunchedEffect(map) {
        val googleMap = map.awaitMap()

        if (clusterManager == null) {
            clusterManager = ClusterManager(context, googleMap)
            clusterManager?.setAnimation(false)
            clusterManager?.setOnClusterItemClickListener {
                navController.navigate("${Screen.Details.route}/${it.id}")
                true
            }
        }

        googleMap.isMyLocationEnabled = locationPermissionState.hasPermission
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.setOnCameraIdleListener {
            if (googleMap.cameraPosition.target.latitude != 0.0 &&
                googleMap.cameraPosition.target.longitude != 0.0
            ) {
                viewModel.setCameraLocation(
                    googleMap.cameraPosition.target,
                    googleMap.cameraPosition.zoom
                )
            }

            googleMap.cameraPosition.target.apply {
                viewModel.setLocationForMapPosition(
                    this,
                    googleMap.projection.visibleRegion.latLngBounds.northeast
                )
            }
            clusterManager?.cluster()
        }
        googleMap.setOnMarkerClickListener(clusterManager)
    }

    clusterManager?.let {
        it.clearItems()
        it.addItems(uiState.mapMarkers)
        clusterManager?.cluster()
    }
}

@ExperimentalPermissionsApi
@Composable
private fun UpdateOnLocationChange(
    uiState: PlacesUiState,
    map: MapView,
    viewModel: PlacesViewModel
) {
    LaunchedEffect(uiState.userLocation) {
        val googleMap = map.awaitMap()
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                uiState.cameraLocation ?: LatLng(
                    uiState.userLocation.latitude,
                    uiState.userLocation.longitude
                ), uiState.zoom
            )
        )
        viewModel.subscribeToNearbyPlaces(
            googleMap.projection.visibleRegion.latLngBounds.center,
            googleMap.projection.visibleRegion.latLngBounds.northeast
        )
    }
}
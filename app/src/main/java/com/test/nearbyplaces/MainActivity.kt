package com.test.nearbyplaces


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.test.nearbyplaces.flow.places.PlacesViewModel
import com.test.nearbyplaces.flow.places.details.PlaceDetailsScreen
import com.test.nearbyplaces.flow.places.listing.PlacesListScreen
import com.test.nearbyplaces.flow.places.map.PlacesMapScreen
import com.test.nearbyplaces.navigation.Screen
import com.test.nearbyplaces.ui.theme.NearbyPlacesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalMaterialApi
    @ExperimentalMaterialNavigationApi
    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val bottomSheetNavigator = rememberBottomSheetNavigator()
            val navController = rememberNavController(bottomSheetNavigator)

            NearbyPlacesTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ModalBottomSheetLayout(bottomSheetNavigator) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Map.route
                        ) {
                            composable(Screen.Map.route) {
                                val placesViewModel = hiltViewModel<PlacesViewModel>()
                                PlacesMapScreen(placesViewModel, navController)
                            }
                            bottomSheet("${Screen.Details.route}/{id}") { entry ->
                                val address = entry.arguments?.getString("id")
                                address?.let {
                                    PlaceDetailsScreen(address = address)
                                }

                            }
                            composable(Screen.List.route) {
                                val parentEntry = remember {
                                    navController.getBackStackEntry(Screen.Map.route)
                                }
                                val placesViewModel = hiltViewModel<PlacesViewModel>(
                                    parentEntry
                                )
                                PlacesListScreen(placesViewModel, navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

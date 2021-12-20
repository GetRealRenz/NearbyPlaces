package com.test.nearbyplaces.flow.places.listing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.test.nearbyplaces.flow.places.PlacesViewModel
import com.test.nearbyplaces.navigation.Screen


@Composable
fun PlacesListScreen(viewModel: PlacesViewModel, navController: NavController) {
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon =
                {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack, contentDescription = "Back"
                        )
                    }
                },
                title = { Text("Nearby Places") }
            )


        }
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(uiState.value.mapMarkers) {
                Box(
                    Modifier
                        .padding(16.dp)
                        .height(48.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("${Screen.Details.route}/${it.id}")
                        },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(text = it.title)
                }


            }
        }
    }
}
package com.test.nearbyplaces.navigation

sealed class Screen(val route: String) {
    object Map : Screen("map")
    object List : Screen("list")
    object Details : Screen("details")
}

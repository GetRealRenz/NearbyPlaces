object Compose {
    const val ui = "androidx.compose.ui:ui:${Versions.compose}"
    const val material = "androidx.compose.material:material:${Versions.compose}"
    const val tooling = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
    const val activity = "androidx.activity:activity-compose:1.4.0"

}
object Android{
    const val core = "androidx.core:core-ktx:1.7.0"
    const val appCompat = "androidx.appcompat:appcompat:1.4.0"
    const val material = "com.google.android.material:material:1.4.0"
}
object Navigation{
    const val compose =  "androidx.navigation:navigation-compose:2.4.0-rc01"
    const val hilt ="androidx.hilt:hilt-navigation-compose:1.0.0-rc01"
}
object Accompanist {

    const val pager = "com.google.accompanist:accompanist-pager:${Versions.accompanist}"
    const val pagerIndicators =
        "com.google.accompanist:accompanist-pager-indicators:${Versions.accompanist}"
    const val permissions = "com.google.accompanist:accompanist-permissions:${Versions.accompanist}"
    const val bottomSheet = "com.google.accompanist:accompanist-navigation-material:${Versions.accompanist}"
}

object BuildDependencies {
    const val buildGradle = "com.android.tools.build:gradle:7.0.4"
    const val kotlinGradle = "org.jetbrains.java:java-gradle-plugin:1.5.21"
    const val hiltGradle = "com.google.dagger:hilt-android-gradle-plugin:2.38.1"
}

object DI {
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
}

object Jetpack {
    const val lifeCycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifeCycle}"
    const val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07"
}

object Database {
    const val room = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
}

object Maps {
    const val arcGis = "com.esri.arcgisruntime:arcgis-android:100.13.0"
    const val location = "com.google.android.gms:play-services-location:${Versions.gms}"
    const val googleMap = "com.google.maps.android:maps-ktx:3.3.0"
    const val googleMapUtils = "com.google.maps.android:maps-utils-ktx:3.3.0"

}

object Concurency {
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9"
    const val coroutinesTask = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.3.9"

}
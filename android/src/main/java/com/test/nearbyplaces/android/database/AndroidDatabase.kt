package com.test.nearbyplaces.android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.nearbyplaces.android.database.dao.PlacesDao
import com.test.nearbyplaces.android.database.models.PlaceLocal

@Database(
    version = 1,
    exportSchema = true,
    entities = [
        PlaceLocal::class,
    ]
)
abstract class AndroidDatabase : RoomDatabase() {
    abstract fun placesDao(): PlacesDao
}

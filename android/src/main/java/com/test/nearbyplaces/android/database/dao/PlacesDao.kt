package com.test.nearbyplaces.android.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.nearbyplaces.android.database.models.PlaceLocal
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PlacesDao {
    @Query("SELECT * FROM placelocal LIMIT 20")
    abstract fun getPlaces(): Flow<List<PlaceLocal>>

    @Query("SELECT * FROM placelocal WHERE id = :address LIMIT 1")
    abstract fun getPlaceByAddress(address: String): Flow<PlaceLocal?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun savePlaces(places: List<PlaceLocal>)

    @Query("DELETE FROM placelocal")
    abstract fun deleteAll()
}
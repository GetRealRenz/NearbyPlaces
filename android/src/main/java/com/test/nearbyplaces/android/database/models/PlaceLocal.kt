package com.test.nearbyplaces.android.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PlaceLocal {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Int = 0

    @ColumnInfo
    var latitude: Double = 0.0

    @ColumnInfo
    var longitude: Double = 0.0

    @ColumnInfo
    var name: String = ""

    @ColumnInfo
    var address: String = ""
}
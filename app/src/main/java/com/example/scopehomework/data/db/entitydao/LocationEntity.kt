package com.example.scopehomework.data.db.entitydao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationEntity(
    @PrimaryKey
    val vehicleid: Int,
    val lat: Double,
    val lon: Double,
    @ColumnInfo(name = "modified_at")
    var modifiedAt: Long
)
package com.example.scopehomework.data.db.entitydao

import androidx.room.Entity

@Entity
data class VehicleEntity(
    val vehicleid: Int,
    val make: String,
    val model: String,
    val year: String,
    val color: String,
    val vin: String,
    val foto: String,
)
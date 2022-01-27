package com.example.scopehomework.data.networking.entitydto

import com.google.gson.annotations.SerializedName

data class VehicleDTO(
    @SerializedName("vehicleid")
    val vehicleid: Int,
    @SerializedName("make")
    val make: String,
    @SerializedName("model")
    val model: String,
    @SerializedName("year")
    val year: String,
    @SerializedName("color")
    val color: String,
    @SerializedName("vin")
    val vin: String,
    @SerializedName("foto")
    val foto: String
)
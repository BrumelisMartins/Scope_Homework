package com.example.scopehomework.data.entity

import com.google.gson.annotations.SerializedName

data class LocationDTO(
    @SerializedName("vehicleid")
    val vehicleid: Int,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double
)
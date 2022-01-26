package com.example.scopehomework.data.entity

import com.google.gson.annotations.SerializedName

data class LocationDataDTO(
    @SerializedName("data")
    val data: List<LocationDTO>?
)
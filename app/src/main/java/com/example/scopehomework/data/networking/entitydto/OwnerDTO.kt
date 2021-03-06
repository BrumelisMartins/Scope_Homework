package com.example.scopehomework.data.networking.entitydto

import com.google.gson.annotations.SerializedName

data class OwnerDTO(
    @SerializedName("name")
    val name: String,
    @SerializedName("surname")
    val surname: String,
    @SerializedName("foto")
    val foto: String
)
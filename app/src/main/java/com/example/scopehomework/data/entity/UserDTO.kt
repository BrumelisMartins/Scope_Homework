package com.example.scopehomework.data.entity

import com.google.gson.annotations.SerializedName

data class UserDTO (
    @SerializedName("userid")
    val userid: Int?,
    @SerializedName("owner")
    val owner: OwnerDTO?,
    @SerializedName("vehicles")
    val vehicles: List<VehicleDTO>?
)
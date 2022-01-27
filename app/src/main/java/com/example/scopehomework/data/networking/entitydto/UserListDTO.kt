package com.example.scopehomework.data.networking.entitydto

import com.google.gson.annotations.SerializedName

class UserListDTO(
    @SerializedName("data")
    val data: List<UserDTO?>
)
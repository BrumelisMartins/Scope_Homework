package com.example.scopehomework.data.entity

import com.google.gson.annotations.SerializedName

class UserListDTO(
    @SerializedName("data")
    val data: List<UserDTO?>
)
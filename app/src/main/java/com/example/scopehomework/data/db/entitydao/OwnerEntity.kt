package com.example.scopehomework.data.db.entitydao

import androidx.room.Entity

@Entity
data class OwnerEntity(
    val name: String,
    val surname: String,
    val foto: String
)
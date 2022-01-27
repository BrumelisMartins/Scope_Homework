package com.example.scopehomework.data.db.entitydao

import androidx.room.*
import com.example.scopehomework.data.db.DataConverter

@Entity
data class UserEntity (
    @PrimaryKey
    val userid: Int?,
    @Embedded
    val owner: OwnerEntity?,
    @TypeConverters(DataConverter::class)
    val vehicles: List<VehicleEntity>?,
    @ColumnInfo(name = "modified_at")
    var modifiedAt: Long
)
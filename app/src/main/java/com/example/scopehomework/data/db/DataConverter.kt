package com.example.scopehomework.data.db

import androidx.room.TypeConverter
import com.example.scopehomework.data.db.entitydao.VehicleEntity
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

@Suppress("UnstableApiUsage")
class DataConverter {

    @TypeConverter
    fun fromVehicleEntityList(value: List<VehicleEntity>): String {
        val gson = Gson()
        val type = object : TypeToken<List<VehicleEntity>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toVehicleEntityList(value: String): List<VehicleEntity> {
        val gson = Gson()
        val type = object : TypeToken<List<VehicleEntity>>() {}.type
        return gson.fromJson(value, type)
    }
}
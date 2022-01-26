package com.example.scopehomework.domain.contract

import com.example.scopehomework.domain.feature.vehiclelocation.entity.LocationData
import com.example.scopehomework.domain.feature.vehiclelocation.entity.User

interface VehicleRepository {
    suspend fun getUserList(): List<User>
    suspend fun getLocationData(id: Int): LocationData
}
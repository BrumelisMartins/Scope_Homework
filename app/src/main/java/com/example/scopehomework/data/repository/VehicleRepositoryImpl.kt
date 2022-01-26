package com.example.scopehomework.data.repository

import com.example.scopehomework.data.mappers.VehicleEntityMapper
import com.example.scopehomework.data.networking.VehicleApiService
import com.example.scopehomework.domain.contract.VehicleRepository
import com.example.scopehomework.domain.feature.vehiclelocation.entity.LocationData
import com.example.scopehomework.domain.feature.vehiclelocation.entity.User
import javax.inject.Inject

class VehicleRepositoryImpl @Inject constructor(
    private val vehicleApiService: VehicleApiService, private val mapper: VehicleEntityMapper
) : VehicleRepository {

    override suspend fun getUserList(): List<User> {
        val list = vehicleApiService.getUserList().data.filter { it?.owner != null }
        return mapper.toUserList(list)
    }

    override suspend fun getLocationData(id: Int): LocationData {
        return mapper.toLocationData(vehicleApiService.getLocationData(id.toString()))
    }
}
package com.example.scopehomework.domain.feature.vehiclelocation.usecase

import com.example.scopehomework.domain.contract.VehicleRepository
import com.example.scopehomework.domain.feature.vehiclelocation.entity.LocationData
import javax.inject.Inject

class GetVehicleLocationUseCase @Inject constructor(
    private val vehicleRepository: VehicleRepository
) {
    suspend fun execute(id: Int): LocationData {
        return vehicleRepository.getLocationData(id)
    }
}
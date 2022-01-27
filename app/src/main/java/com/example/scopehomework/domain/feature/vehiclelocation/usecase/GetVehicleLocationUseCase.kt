package com.example.scopehomework.domain.feature.vehiclelocation.usecase

import com.example.scopehomework.domain.contract.VehicleRepository
import com.example.scopehomework.domain.feature.vehiclelocation.entity.Location
import javax.inject.Inject

class GetVehicleLocationUseCase @Inject constructor(
    private val vehicleRepository: VehicleRepository
) {
    suspend fun execute(id: Int): List<Location> {
        return vehicleRepository.getLocationData(id)
    }
}
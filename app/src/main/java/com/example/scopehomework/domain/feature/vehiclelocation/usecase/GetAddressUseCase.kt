package com.example.scopehomework.domain.feature.vehiclelocation.usecase

import com.example.scopehomework.domain.contract.MapRepository
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(
    private val mapRepository: MapRepository
) {
    suspend fun execute(lat: Double, lon: Double, vehicleID: Int): String {
        return mapRepository.getAddress(lat, lon, vehicleID)
    }
}
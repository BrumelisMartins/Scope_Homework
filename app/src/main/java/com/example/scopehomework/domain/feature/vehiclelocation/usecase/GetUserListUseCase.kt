package com.example.scopehomework.domain.feature.vehiclelocation.usecase

import com.example.scopehomework.domain.contract.VehicleRepository
import com.example.scopehomework.domain.feature.vehiclelocation.entity.User
import javax.inject.Inject

class GetUserListUseCase @Inject constructor(
    private val vehicleRepository: VehicleRepository
) {
    suspend fun execute(): List<User> {
        return vehicleRepository.getUserList()
    }
}
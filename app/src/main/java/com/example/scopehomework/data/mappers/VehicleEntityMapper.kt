package com.example.scopehomework.data.mappers

import com.example.scopehomework.data.entity.LocationDataDTO
import com.example.scopehomework.data.entity.UserDTO
import com.example.scopehomework.domain.feature.vehiclelocation.entity.LocationData
import com.example.scopehomework.domain.feature.vehiclelocation.entity.User
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE)
interface VehicleEntityMapper {

    @InheritInverseConfiguration
    fun toLocationData(locationDataDTO: LocationDataDTO): LocationData

    @InheritInverseConfiguration
    fun toUserList(userList: List<UserDTO?>): List<User>
}
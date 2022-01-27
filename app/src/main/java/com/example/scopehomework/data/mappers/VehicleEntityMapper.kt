package com.example.scopehomework.data.mappers

import com.example.scopehomework.data.db.entitydao.LocationEntity
import com.example.scopehomework.data.db.entitydao.UserEntity
import com.example.scopehomework.data.networking.entitydto.LocationDTO
import com.example.scopehomework.data.networking.entitydto.LocationDataDTO
import com.example.scopehomework.data.networking.entitydto.UserDTO
import com.example.scopehomework.domain.feature.vehiclelocation.entity.Location
import com.example.scopehomework.domain.feature.vehiclelocation.entity.User
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE)
interface VehicleEntityMapper {

    @InheritInverseConfiguration
    fun userDtoListToUserList(userList: List<UserDTO?>): List<User>

    @InheritInverseConfiguration
    fun userDtoListToUserEntityList(userList: List<UserDTO?>): List<UserEntity>

    @InheritInverseConfiguration
    fun userEntityListToUserList(userEntityList: List<UserEntity>): List<User>


    @InheritInverseConfiguration
    fun locationDtoListToLocationList(locationDataDTO: List<LocationDTO>): List<Location>

    @InheritInverseConfiguration
    fun locationDtoListToLocationEntityList(locationData: List<LocationDTO>): List<LocationEntity>

    @InheritInverseConfiguration
    fun locationEntityListToLocationList(locationData: List<LocationEntity>): List<Location>


}
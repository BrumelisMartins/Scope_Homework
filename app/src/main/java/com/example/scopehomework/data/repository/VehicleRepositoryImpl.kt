package com.example.scopehomework.data.repository

import com.example.scopehomework.data.db.UserDao
import com.example.scopehomework.data.mappers.VehicleEntityMapper
import com.example.scopehomework.data.networking.VehicleApiService
import com.example.scopehomework.domain.contract.VehicleRepository
import com.example.scopehomework.domain.feature.vehiclelocation.entity.Location
import com.example.scopehomework.domain.feature.vehiclelocation.entity.User
import java.util.*
import javax.inject.Inject

class VehicleRepositoryImpl @Inject constructor(
    private val vehicleApiService: VehicleApiService,
    private val mapper: VehicleEntityMapper,
    private val userDao: UserDao
) : VehicleRepository {

    private val millisThreshHoldLocationData = 30000
    override suspend fun getUserList(): List<User> {
        val localList = userDao.getUsers()

        return if (isUserDataFresh(localList.firstOrNull()?.modifiedAt)){
            // returns local data
            mapper.userEntityListToUserList(localList)
        } else {
            // returns data from API
            val list = vehicleApiService.getUserList().data.filter { it?.owner != null }
            userDao.insertAllUsersWithTimestamp(mapper.userDtoListToUserEntityList(list))
            mapper.userDtoListToUserList(list)
        }
    }

    private fun isUserDataFresh(timestamp: Long?): Boolean {
        timestamp ?: return false
        val today = Calendar.getInstance()
        val dataDate = Calendar.getInstance().apply {
            timeInMillis = timestamp
        }
        return today.get(Calendar.DAY_OF_YEAR) == dataDate.get(Calendar.DAY_OF_YEAR)
    }


    override suspend fun getLocationData(id: Int): List<Location> {
        val localList = userDao.getLocations()

        return if (isLocationDataFresh(localList.firstOrNull()?.modifiedAt)){
            //returns cached data
            mapper.locationEntityListToLocationList(localList)
        } else {
            val list = vehicleApiService.getLocationData(id.toString()).data
            userDao.insertAllLocationsWithTimestamp(mapper.locationDtoListToLocationEntityList(list))
            mapper.locationDtoListToLocationList(list)
        }
    }


    private fun isLocationDataFresh(timestamp: Long?): Boolean {
        timestamp ?: return false
        return Calendar.getInstance().timeInMillis - timestamp < millisThreshHoldLocationData
    }
}
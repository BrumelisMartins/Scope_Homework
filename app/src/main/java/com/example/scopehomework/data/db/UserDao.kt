package com.example.scopehomework.data.db

import androidx.room.*
import com.example.scopehomework.data.db.entitydao.LocationEntity
import com.example.scopehomework.data.db.entitydao.UserEntity

@Dao
abstract class UserDao {

    @Transaction
    @Query("SELECT * FROM UserEntity")
    abstract suspend fun getUsers(): List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllUsers(userEntity: List<UserEntity>)

    suspend fun insertAllUsersWithTimestamp(userEntity: List<UserEntity>) {
        insertAllUsers(userEntity.apply {
            this.map { it.modifiedAt = System.currentTimeMillis() }
        })
    }


    @Transaction
    @Query("SELECT * FROM LocationEntity")
    abstract suspend fun getLocations(): List<LocationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllLocations(userLocations: List<LocationEntity>)

    suspend fun insertAllLocationsWithTimestamp(userLocations: List<LocationEntity>) {
        insertAllLocations(userLocations.apply {
            this.map { it.modifiedAt = System.currentTimeMillis() }
        })
    }


}
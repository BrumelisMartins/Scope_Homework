package com.example.scopehomework.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.scopehomework.data.db.entitydao.LocationEntity
import com.example.scopehomework.data.db.entitydao.UserEntity

@Database(entities = [UserEntity::class, LocationEntity::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

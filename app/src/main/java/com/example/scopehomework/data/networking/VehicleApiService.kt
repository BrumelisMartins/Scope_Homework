package com.example.scopehomework.data.networking

import com.example.scopehomework.data.networking.entitydto.LocationDataDTO
import com.example.scopehomework.data.networking.entitydto.UserListDTO
import retrofit2.http.GET
import retrofit2.http.Query


interface VehicleApiService {
    @GET("?op=list")
    suspend fun getUserList(): UserListDTO

    @GET("?op=getlocations")
    suspend fun getLocationData(@Query("userid") id: String): LocationDataDTO
}


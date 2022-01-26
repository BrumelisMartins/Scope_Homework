package com.example.scopehomework.data.repository

import android.content.res.Resources
import com.example.scopehomework.R
import com.example.scopehomework.data.networking.MapApiService
import com.example.scopehomework.domain.contract.MapRepository
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.geojson.Point
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

@ExperimentalCoroutinesApi
class MapRepositoryImpl @Inject constructor(val resources: Resources,  val mapApiService: MapApiService) : MapRepository {

    override suspend fun getAddress(lat: Double, lon: Double, vehicleID: Int): String {
        return mapApiService.getAddress(lat, lon, vehicleID)
    }

    override suspend fun getRoute(origin: Point, destination: Point): DirectionsRoute {
        return mapApiService.getRoute(origin, destination)
    }
}
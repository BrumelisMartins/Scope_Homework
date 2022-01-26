package com.example.scopehomework.data.networking

import android.content.res.Resources
import com.example.scopehomework.R
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

class MapApiService @Inject constructor(val resources: Resources) {

    suspend fun getAddress(lat: Double, lon: Double, vehicleID: Int): String =
        suspendCancellableCoroutine { continuation ->
            val token = resources.getString(R.string.mapbox_access_token)
            MapboxGeocoding.builder()
                .accessToken(token)
                .query(Point.fromLngLat(lon, lat))
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .build().enqueueCall(object : Callback<GeocodingResponse> {
                    override fun onResponse(
                        call: Call<GeocodingResponse>,
                        response: Response<GeocodingResponse>
                    ) {
                        val address = if (response.body()?.features().isNullOrEmpty()) {
                            resources.getString(R.string.location_unknown)
                        } else {
                            response.body()!!.features()[0].placeName()!!
                        }
                        continuation.resume(address) {

                        }
                    }

                    override fun onFailure(call: Call<GeocodingResponse>, t: Throwable) {
                        continuation.resume(resources.getString(R.string.location_unknown)){

                        }
                    }

                })
        }

    suspend fun getRoute(origin: Point, destination: Point): DirectionsRoute =
        suspendCancellableCoroutine { continuation ->
            val token = resources.getString(R.string.mapbox_access_token)
            MapboxDirections.builder()
                .accessToken(token)
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .build().enqueueCall(object : Callback<DirectionsResponse> {
                    override fun onResponse(
                        call: Call<DirectionsResponse>,
                        response: Response<DirectionsResponse>
                    ) {
                        if (response.body() == null || response.body()!!.routes().size < 1) {
                            continuation.resumeWithException(Throwable())
                        } else if (response.body()!!.routes().size < 1) {
                            continuation.resumeWithException(Throwable())
                        } else {
                            continuation.resume(response.body()!!.routes().first()) {

                            }
                        }

                    }

                    override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                        continuation.resumeWithException(Throwable())
                    }

                })
        }
}
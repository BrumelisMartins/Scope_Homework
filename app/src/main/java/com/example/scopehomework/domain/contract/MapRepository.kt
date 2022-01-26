package com.example.scopehomework.domain.contract

import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point

interface MapRepository {
    suspend fun getAddress(lat: Double, lon: Double, vehicleID: Int): String
    suspend fun getRoute(origin: Point, destination: Point): DirectionsRoute
}
package com.example.scopehomework.domain.feature.vehiclelocation.usecase

import com.example.scopehomework.domain.contract.MapRepository
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import javax.inject.Inject

class GetRouteUseCase @Inject constructor(private val mapRepository: MapRepository) {
    suspend fun execute(origin: Point, destination: Point): DirectionsRoute{
        return mapRepository.getRoute(origin, destination)
    }
}

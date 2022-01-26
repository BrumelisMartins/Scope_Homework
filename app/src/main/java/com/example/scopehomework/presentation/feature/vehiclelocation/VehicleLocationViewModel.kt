package com.example.scopehomework.presentation.feature.vehiclelocation

import android.annotation.SuppressLint
import android.content.res.Resources
import android.location.LocationManager
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import com.example.scopehomework.R
import com.example.scopehomework.domain.feature.vehiclelocation.entity.Location
import com.example.scopehomework.domain.feature.vehiclelocation.entity.Vehicle
import com.example.scopehomework.domain.feature.vehiclelocation.usecase.GetAddressUseCase
import com.example.scopehomework.domain.feature.vehiclelocation.usecase.GetRouteUseCase
import com.example.scopehomework.domain.feature.vehiclelocation.usecase.GetVehicleLocationUseCase
import com.example.scopehomework.presentation.State
import com.example.scopehomework.utils.Utils
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.annotations.Icon
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


@HiltViewModel
class VehicleLocationViewModel @Inject constructor(
    private val locationUseCase: GetVehicleLocationUseCase,
    private val addressUseCase: GetAddressUseCase,
    private val routeUseCase: GetRouteUseCase,
    private val iconFactory: IconFactory,
    private val resources: Resources,
    private val locationManager: LocationManager
) :
    ViewModel() {

    val sourceID = "SOURCE_ID"
    val directionsLayerID = "DIRECTIONS_LAYER_ID"
    val layerBelowID = "road-label-small"
    val padding = 200

    fun getVehicleLocation(userId: Int) = flow {
        emit(State.LoadingState)
        emit(State.DataState(locationUseCase.execute(userId)))
    }.catch { e ->
        e.printStackTrace()
        emit(Utils.resolveError(e))
    }


    fun getRoute(origin: Point, destination: Point) = flow {
        emit(State.LoadingState)
        emit(State.DataState(routeUseCase.execute(origin, destination)))
    }.catch { e ->
        e.printStackTrace()
        emit(Utils.resolveError(e))
    }

    private suspend fun getAddress(lat: Double, lon: Double, vehicleID: Int) : String {
        return addressUseCase.execute(lat, lon, vehicleID)
    }


    suspend fun getVehicleData(
        locationData: List<Location>,
        vehicleList: List<Vehicle>
    ): List<VehicleData> = coroutineScope {
        val vehicleDataList = arrayListOf<VehicleData>()
        locationData.forEach {

            val vehicle = vehicleList.find { vehicle -> vehicle.vehicleid == it.vehicleid }
            vehicle?.let { realVehicle ->
                val vehicleMame = "${realVehicle.make} ${realVehicle.model}"
                val address = async { getAddress(it.lat, it.lon, it.vehicleid) }
                vehicleDataList.add(
                    VehicleData(
                        image = realVehicle.foto,
                        name = vehicleMame,
                        address = address.await(),
                        color = realVehicle.color,
                        point = LatLng(it.lat, it.lon)
                    )
                )
            }
        }
        vehicleDataList
    }

    fun getIcon(color: Int): Icon{
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_map_marker ,null)
        drawable?.setTint(color)
        val height = resources.getDimension(R.dimen.icon_height).toInt()
        val width = resources.getDimension(R.dimen.icon_width).toInt()
        return iconFactory.fromBitmap(drawable?.toBitmap(width, height)!!)
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): android.location.Location?{

        var currentLocation: android.location.Location? = null

        val locationByGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val locationByNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)


        locationByGps?.let {
            currentLocation = locationByGps
        }

        locationByNetwork?.let {
            currentLocation = locationByNetwork
        }

        if (locationByNetwork != null && locationByGps != null) {
            //if both are available choose he most accurate
            currentLocation = if (locationByNetwork.accuracy > locationByNetwork.accuracy) {
                locationByGps
            } else {
                locationByNetwork
            }
        }
        return currentLocation
    }
}
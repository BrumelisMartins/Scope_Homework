package com.example.scopehomework.presentation.feature.vehiclelocation

import com.mapbox.mapboxsdk.geometry.LatLng


data class VehicleData(val image: String, val name: String, val address: String, val color: String, val point: LatLng)

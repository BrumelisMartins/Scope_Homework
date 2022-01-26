package com.example.scopehomework.domain.feature.vehiclelocation.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vehicle(
    val vehicleid: Int,
    val make: String,
    val model: String,
    val year: String,
    val color: String,
    val vin: String,
    val foto: String
) : Parcelable
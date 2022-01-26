package com.example.scopehomework.domain.feature.vehiclelocation.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val userid: Int?,
    val owner: Owner?,
    val vehicles: List<Vehicle>?
) : Parcelable
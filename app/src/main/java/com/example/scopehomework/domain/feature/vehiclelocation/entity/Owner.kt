package com.example.scopehomework.domain.feature.vehiclelocation.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Owner(
    val name: String,
    val surname: String,
    val foto: String
) : Parcelable
package com.example.scopehomework.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.example.scopehomework.presentation.AuthenticationException
import com.example.scopehomework.presentation.NetworkErrorException
import com.example.scopehomework.presentation.State
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class Utils {
    companion object{
        fun resolveError(e: Throwable): State.ErrorState {
            var error = e

            when (e) {
                is SocketTimeoutException -> {
                    error = NetworkErrorException(errorMessage = "connection error!")
                }
                is ConnectException -> {
                    error = NetworkErrorException(errorMessage = "no internet access!")
                }
                is UnknownHostException -> {
                    error = NetworkErrorException(errorMessage = "no internet access!")
                }
                else -> {
                    error = NetworkErrorException(errorMessage = "unknown")
                }
            }

            if(e is HttpException){
                when(e.code()){
                    502 -> {
                        error = NetworkErrorException(e.code(),  "internal error!")
                    }
                    401 -> {
                        throw AuthenticationException("authentication error!")
                    }
                    400 -> {
                        error = NetworkErrorException.parseException(e)
                    }
                }
            }


            return State.ErrorState(error)
        }
    }
}


fun Fragment.isGranted(permission: AppPermission) = run {
    context?.let {
        (PermissionChecker.checkSelfPermission(it, permission.permissionName
        ) == PermissionChecker.PERMISSION_GRANTED)
    } ?: false
}

fun Fragment.shouldShowRationale(permission: AppPermission) = run {
    shouldShowRequestPermissionRationale(permission.permissionName)
}

fun Fragment.requestPermission(permission: AppPermission) {
    requestPermissions(arrayOf(permission.permissionName), permission.requestCode
    )
}

fun AppCompatActivity.checkPermission(permission: AppPermission) = run {
    ActivityCompat.checkSelfPermission(this, permission.permissionName
        ) == PermissionChecker.PERMISSION_GRANTED
}

fun AppCompatActivity.shouldRequestPermissionRationale(permission: AppPermission) =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission.permissionName)

fun AppCompatActivity.requestAllPermissions(permission: AppPermission) {
    ActivityCompat.requestPermissions(this, arrayOf(permission.permissionName), permission.requestCode)
}

sealed class AppPermission(
    val permissionName: String, val requestCode: Int) {
    companion object {
        val permissions: List<AppPermission> by lazy {
            listOf(
                ACCESS_FINE_LOCATION
            )
        }
    }

    object ACCESS_FINE_LOCATION : AppPermission(Manifest.permission.ACCESS_FINE_LOCATION, 42)
}
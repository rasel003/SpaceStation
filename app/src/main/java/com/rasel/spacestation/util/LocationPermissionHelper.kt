package com.rasel.spacestation.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Build
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

object LocationPermissionHelper {
    const val LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION


    fun getCountryFromLatLng(
        context: Context,
        latitude: Double,
        longitude: Double,
        callback: (String?) -> Unit
    ) {
        val geocoder = Geocoder(context, Locale.getDefault())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                val country = addresses.firstOrNull()?.countryName
                callback(country)
            }
        } else {
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                val country = addresses?.firstOrNull()?.countryName
                callback(country)
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getUserCountryByGPS(
        context: Context,
        callback: (String?) -> Unit
    ) {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                getCountryFromLatLng(
                    context,
                    latitude = location.latitude,
                    longitude = location.longitude
                ) {
                    callback(it)
                }

            } else {
                callback(null)
            }
        }
    }


}
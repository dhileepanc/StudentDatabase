package com.app.studenttask.ui.utils

import android.content.Context
import android.location.Geocoder
import java.util.Locale

object LocationUtils {
    fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val street = address.thoroughfare ?: ""
                val subLocality = address.subLocality ?: ""
                val locality = address.locality ?: ""
                
                listOfNotNull(street.ifBlank { null }, subLocality.ifBlank { null }, locality.ifBlank { null })
                    .joinToString(", ")
                    .ifBlank { "Unknown Location" }
            } else {
                "Unknown Location"
            }
        } catch (e: Exception) {
            "Unknown Location"
        }
    }
}

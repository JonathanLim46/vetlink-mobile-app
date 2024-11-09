package com.example.vetlink

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.util.Locale

object LocationPermissionHelper {

    private val BASIC_PERMISSION = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    val BASIC_PERMISSION_REQUESTCODE = 0

    fun hasAccessCoarsePermission(activity: Activity): Boolean{
        return ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestCoarseLocationPermission(activity: Activity){
        ActivityCompat.requestPermissions(activity!!, BASIC_PERMISSION, BASIC_PERMISSION_REQUESTCODE)
    }

    fun shouldShowRequestPermissionRationale(activity: Activity): Boolean{
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, BASIC_PERMISSION.get(0))
    }

    fun launchPermissionSettings(activity: Activity) {
        val intent = Intent()
        intent.action = android.provider.Settings.ACTION_APPLICATION_SETTINGS
        intent.data = Uri.fromParts("package", activity.packageName, null)
        activity.startActivity(intent)
    }

    fun checkLocationPermission(activity: Activity){
        if(!hasAccessCoarsePermission(activity)){
            Toast.makeText(activity, "Location permission is required to access this feature", Toast.LENGTH_LONG).show()
            requestCoarseLocationPermission(activity)
        }
        val locationManager = activity.getSystemService(Activity.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            promptToEnableGPS(activity)
        }
    }

    private fun promptToEnableGPS(activity: Activity){
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity.startActivity(intent)
    }

    fun getCityFromLocation(context: Context, latitude: Double, longitude: Double): String? {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]

                return address.subAdminArea?.replace("Kota", "")?.replace("Kabupaten", "")?.trim()
                    ?: address.locality?.replace("Kecamatan", "")?.trim()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


}
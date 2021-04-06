package com.ahmed.weatherapptask.Utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment


class PermissionManager() {


    private val MIXED_PERMISSION = 150
    val locationPermission = 200


    fun checkForPermissions(fragment: Fragment, permissions: Array<String>) {
        val permissionsNeeded: ArrayList<String> = ArrayList()
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(fragment.requireActivity(), permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsNeeded.add(permission)
            }
        }
        if (permissionsNeeded.isNotEmpty()) {
            requestPermission(
                fragment,
                permissionsNeeded.toArray(arrayOfNulls(permissionsNeeded.size))
            )
        }
    }

    /**
     * To Request Needed
     * @param permissions
     * */
    private fun requestPermission(fragment: Fragment, permissions: Array<String>) {
        fragment.requestPermissions(permissions, MIXED_PERMISSION)
    }


    /**
     * @return true if the
     * @param permission is granted
     * */
    fun isPermissionGranted(fragment: Fragment, permission: String?): Boolean {
        return (ActivityCompat.checkSelfPermission(fragment.requireActivity(), permission!!)
                == PackageManager.PERMISSION_GRANTED)
    }

    /**
     * @return true if All
     * @param permissions are granted
     * */
    fun isAllPermissionGranted(fragment: Fragment, permissions: Array<String>): Boolean {
        var isAllPermissionsGranted = true
        for (permission in permissions) {
            if (!isPermissionGranted(fragment, permission)) {
                isAllPermissionsGranted = false
                break
            }
        }
        return isAllPermissionsGranted
    }

    /**
     * To request location Permission
     * */
    fun requestPermissionLocationPermission(fragment: Fragment) {
        requestPermission(
            fragment,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        )

    }
}



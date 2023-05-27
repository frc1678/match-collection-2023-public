// Copyright (c) 2023 FRC Team 1678: Citrus Circuits
package com.frc1678.match_collection

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

// Retrieve serial number of device.
fun getSerialNum(context: Context): String? {
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return Build.getSerial()
    }
    return null
}

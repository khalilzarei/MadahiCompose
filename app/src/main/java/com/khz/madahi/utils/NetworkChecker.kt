// utils/NetworkChecker.kt
package com.khz.madahi.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat

class NetworkChecker(private val context: Context) {

    fun isNetworkConnected(): Boolean {
        val connectivityManager = ContextCompat.getSystemService(
            context,
            ConnectivityManager::class.java
        )
                ?: return false

        val network = connectivityManager.activeNetwork
                ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network)
                ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
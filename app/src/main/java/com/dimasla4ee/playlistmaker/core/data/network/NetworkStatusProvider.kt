package com.dimasla4ee.playlistmaker.core.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkStatusProvider(
    context: Context
) {

    private val connectivityManager = context.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager

    val isConnected: Boolean
        get() {
            val activeNetwork = connectivityManager.activeNetwork ?: return false

            val capabilities = connectivityManager.getNetworkCapabilities(
                activeNetwork
            ) ?: return false

            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }

}
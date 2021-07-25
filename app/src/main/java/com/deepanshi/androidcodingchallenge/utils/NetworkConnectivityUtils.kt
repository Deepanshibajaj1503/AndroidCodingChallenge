package com.deepanshi.androidcodingchallenge.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import com.deepanshi.androidcodingchallenge.AndroidCodingChallengeApplication

class NetworkConnectivityUtils {

    companion object {

        /**
         * Unregister callback on components life cycle
         */
        fun unRegisterConnectivityCallback(callback: ConnectivityManager.NetworkCallback) {
            val connectivityManager = AndroidCodingChallengeApplication.applicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            connectivityManager?.unregisterNetworkCallback(callback)
        }

        /**
         * Register callback for network connectivity
         */
        fun registerConnectivityCallback(callback: ConnectivityManager.NetworkCallback) {
            val connectivityManager = AndroidCodingChallengeApplication.applicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager?.registerDefaultNetworkCallback(callback)
            } else {
                val request = NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
                connectivityManager?.registerNetworkCallback(request, callback)
            }
        }
    }

}
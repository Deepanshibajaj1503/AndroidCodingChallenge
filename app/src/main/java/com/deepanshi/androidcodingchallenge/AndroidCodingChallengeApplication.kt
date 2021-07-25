package com.deepanshi.androidcodingchallenge

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class AndroidCodingChallengeApplication : Application() {

    private val TAG = "Application"

    init {
        instance = this
    }

    companion object {
        private var instance: AndroidCodingChallengeApplication? = null

        fun applicationContext() : AndroidCodingChallengeApplication {
            return instance as AndroidCodingChallengeApplication
        }

    }

    override fun onCreate() {
        super.onCreate()
    }

    /**
     * Checking the internet connectivity
     *
     * @return true if the connection is available otherwise false
     */
    fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}
package com.deepanshi.androidcodingchallenge.ui.base

import android.util.Log
import androidx.fragment.app.Fragment
import com.deepanshi.androidcodingchallenge.utils.ConnectionStateCheck


open class BaseFragment : Fragment(), ConnectionStateCheck.OnNetworkAvailableCallbacks {

    private var connectionStateCheck: ConnectionStateCheck? = null


    override fun onResume() {
        super.onResume()

        if (connectionStateCheck == null)
            connectionStateCheck = this.activity?.let { ConnectionStateCheck(it, this) }

        //Register
        connectionStateCheck?.enable()

        // Recheck network status manually whenever activity resumes
        if (connectionStateCheck?.hasNetworkConnection() == false) onNetworkLost()
        else onNetworkAvailable()
    }


    override fun onPause() {
        connectionStateCheck?.disable() //Unregister
        connectionStateCheck = null
        super.onPause()
    }


    override fun onNetworkAvailable() {
        Log.d("Network", "Network connected!")
    }

    override fun onNetworkLost() {
        Log.d("Network", "Network Lost!")
    }

}
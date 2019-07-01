package com.brainplow.ogrespace.kotlin

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionDetector(context: Context) {


    val context: Context =context

    fun isconnected() :Boolean
    {

        val connectivity: ConnectivityManager =context.getSystemService(Service.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(connectivity!=null)
        {
            var networkInfo: NetworkInfo?=connectivity?.activeNetworkInfo
            if(networkInfo!=null)
            {

                if(networkInfo.state== NetworkInfo.State.CONNECTED)
                {
                    return true
                }

            }
            return false



        }
        return true
    }
}
package com.brainplow.ogrespace.kotlin

import android.app.Application
import com.facebook.FacebookSdk

class Initializer : Application() {
    override fun onCreate() {
        super.onCreate()

        FacebookSdk.sdkInitialize(applicationContext)
    }

}
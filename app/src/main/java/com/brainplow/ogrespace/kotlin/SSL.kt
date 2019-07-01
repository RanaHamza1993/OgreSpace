package com.brainplow.ogrespace.kotlin

import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object SSL {


    fun sslCertificates() {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate>? {
                return null
            }

            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}

            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
        })
        var sc: SSLContext? = null
        try {
            sc = SSLContext.getInstance("SSL")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        try {
            sc!!.init(null, trustAllCerts, java.security.SecureRandom())
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(sc!!.socketFactory)
        // Create all-trusting host name verifier
        val allHostsValid = HostnameVerifier { hostname, session ->
            session.localCertificates
            if (hostname == "apis.officedoor.ai")
                true
            else if(hostname=="storage.officedoor.ai")
                true
//                else if (hostname=="storage.cramfrenzy.com/upload_image.php")
//                    true
            else if(hostname=="graph.facebook.com")
                true

            else
                false
        }
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)


    }

}
package com.brainplow.ogrespace.kotlin

import android.util.Base64

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class JWTUtils {

    companion object {

        @Throws(Exception::class)
        fun decoded(JWTEncoded: String?): String {
            val split: Array<String>?
            var abc = "abc"
            try {
                split = JWTEncoded?.split("\\.".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
                abc = getJson(split?.get(1))
            } catch (e: UnsupportedEncodingException) {
                //Error

            }

            return abc
        }

        @Throws(UnsupportedEncodingException::class)
        private fun getJson(strEncoded: String?): String {
            val decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE)
            return String(decodedBytes, Charset.forName("UTF-8"))
        }
    }

}

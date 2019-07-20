package com.brainplow.ogrespace.kotlin

import android.content.Context
import com.android.volley.Cache
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.error.VolleyError
import com.android.volley.request.JsonObjectRequest
import com.android.volley.Response
import com.android.volley.request.JsonArrayRequest
import com.android.volley.request.StringRequest
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap
import com.android.volley.toolbox.HttpHeaderParser
import android.R.attr.data
import android.R.attr.data
import android.util.Log
import com.android.volley.error.ParseError
import org.json.JSONException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class getStringData constructor(val context: Context?,val urlString: String?,val token: String?,
val continuation: Continuation<String>
)  : StringRequest(Request.Method.GET, urlString,
Response.Listener { response ->
    continuation.resume(response)
},
Response.ErrorListener { error ->

    continuation.resume(error.networkResponse.toString())

}
)
{


    override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
        return super.parseNetworkResponse(response)
    }

    override fun getHeaders(): MutableMap<String, String> {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/text; charset=utf-8"
        if (!token.equals(""))
            headers.put("Authorization", "JWT " + token)
        return headers
    }
    fun getStringRequest(){
        getStringData(context,urlString,token,continuation).also {
            it.setShouldCache(false)
            MySingleton.getInstance(context!!.applicationContext).addToRequestQueue(it)

        }
    }
}
class getStringDataFlag constructor(val context: Context?, private val urlString: String?, val token: String?, private val urlflag:String?,
                                    private val continuation: Continuation<String>
)  : StringRequest(Request.Method.GET, urlString ,
        Response.Listener { resp ->
            continuation.resume(resp)
        },
        Response.ErrorListener { error ->

            continuation.resume(error.toString())

        }
)
{


    override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
        return super.parseNetworkResponse(response)
    }

    override fun getHeaders(): MutableMap<String, String> {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/text; charset=utf-8"
//        if (!PaymentMethodFragment.token.equals(""))
            headers.put("Authorization", "JWT " + token)
        return headers
    }
    fun getStringRequest(){
        getStringDataFlag(context,urlString,token,urlflag,continuation).also {
            it.setShouldCache(false)
            MySingleton.getInstance(context!!.applicationContext).addToRequestQueue(it)

        }
    }
}

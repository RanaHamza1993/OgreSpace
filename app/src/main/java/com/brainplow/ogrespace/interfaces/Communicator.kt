package com.brainplow.ogrespace.interfaces

import com.android.volley.error.VolleyError
import org.json.JSONArray
import org.json.JSONObject

interface Communicator {
    interface IVolleResult{

        fun notifySuccess(requestType: String?, response: JSONObject?, url:String, netWorkResponse:Int?=0){}
        fun notifySuccess(requestType: String?, response: JSONArray?, url:String){}
        fun notifySuccess(requestType: String?, response: String?,url:String){}
        fun notifyError(requestType: String?, error: VolleyError?, url:String){}
    }
}
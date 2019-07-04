package com.brainplow.ogrespace.interfaces

import com.android.volley.error.VolleyError
import com.brainplow.ogrespace.enums.RequestType
import org.json.JSONArray
import org.json.JSONObject

interface Communicator {
    interface IVolleResult{

        fun notifySuccess(requestType: RequestType?, response: JSONObject?, url:String, netWorkResponse:Int?=0){}
        fun notifySuccess(requestType: RequestType?, response: JSONArray?, url:String, netWorkResponse:Int?=0){}
        fun notifySuccess(requestType: RequestType?, response: String?,url:String, netWorkResponse:Int?=0){}
        fun notifyError(requestType: RequestType?, error: VolleyError?, url:String, netWorkResponse:Int?=0){}
    }
    interface IActionBar{
        fun actionBarListener(title: String)
        fun isBackButtonEnabled(isEnabled:Boolean)
        fun isSearchVisible(isVisible:Boolean)
        fun toolbarColor(isWhite:Boolean) {

        }

        fun backArrow(isWhite: Boolean) {

        }

        fun optionMenuVisibility(isVisible: Boolean) {

        }
        fun searchlogo(isVisible: Boolean) {

        }

        fun toolbarBackground(isWhite: Boolean) {

        }
    }
    interface IBottomBar {
        fun isBottomVisible(isVisible: Boolean)
    }
    interface IStates{
        fun onStateItemClick(id:Int?,name:String?){}
    }
}
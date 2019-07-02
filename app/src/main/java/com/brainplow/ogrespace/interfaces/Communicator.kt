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
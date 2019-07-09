package com.brainplow.ogrespace.kotlin

import android.util.Log
import com.brainplow.ogrespace.models.PropertyModel
import com.brainplow.ogrespace.models.StateModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject

class VolleyParsing {
    var stateList = ArrayList<StateModel>()
    var propertyList = ArrayList<PropertyModel>()
    val stateType=object: TypeToken<ArrayList<StateModel>>(){}.type
    val propertyType=object: TypeToken<ArrayList<PropertyModel>>(){}.type
    fun getStateData(response: JSONArray?, pageNo: Int): ArrayList<StateModel> {
        if (pageNo == 1)
            stateList.clear()
        try {
            //   val array = JSONObject(response)
            // val marry: JSONArray = array.get("courses") as JSONArray
            stateList.addAll(GsonBuilder().serializeNulls().create().fromJson(response.toString(),stateType))
        } catch (e:Exception) {
            Log.d("error",e.toString())
        }
        return stateList
    }
    fun getPropertyData(response: JSONObject?, pageNo: Int): ArrayList<PropertyModel> {
        if (pageNo == 1)
            propertyList.clear()
        try {
            //   val array = JSONObject(response)
            var marry: JSONArray?=null
            try {
                marry = response?.get("Results") as JSONArray
            }catch (e:Exception){
                marry = response?.get("results") as JSONArray
            }
            propertyList.addAll(GsonBuilder().serializeNulls().create().fromJson(marry.toString(),propertyType))
        } catch (e:Exception) {
            Log.d("error",e.toString())
        }
        return propertyList
    }

}
package com.brainplow.ogrespace.kotlin

import android.util.Log
import com.brainplow.ogrespace.models.CategoriesModel
import com.brainplow.ogrespace.models.MyFavModel
import com.brainplow.ogrespace.models.PropertyModel
import com.brainplow.ogrespace.models.StateModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject

class VolleyParsing {
    var stateList = ArrayList<StateModel>()
    var catList = ArrayList<CategoriesModel>()
    var propertyList = ArrayList<PropertyModel>()
    var favModel = ArrayList<MyFavModel>()
    val stateType = object : TypeToken<ArrayList<StateModel>>() {}.type
    val catType = object : TypeToken<ArrayList<CategoriesModel>>() {}.type
    val propertyType = object : TypeToken<ArrayList<PropertyModel>>() {}.type
    val favType = object : TypeToken<ArrayList<MyFavModel>>() {}.type
    fun getStateData(response: JSONArray?, pageNo: Int): ArrayList<StateModel> {
        if (pageNo == 1)
            stateList.clear()
        try {
            //   val array = JSONObject(response)
            // val marry: JSONArray = array.get("courses") as JSONArray
            stateList.addAll(GsonBuilder().serializeNulls().create().fromJson(response.toString(), stateType))
        } catch (e: Exception) {
            Log.d("error", e.toString())
        }
        return stateList
    }
    fun getCategoriesData(response: JSONArray?): ArrayList<CategoriesModel> {
            catList.clear()
        try {
            //   val array = JSONObject(response)
            // val marry: JSONArray = array.get("courses") as JSONArray
            catList.addAll(GsonBuilder().serializeNulls().create().fromJson(response.toString(), catType))
        } catch (e: Exception) {
            Log.d("error", e.toString())
        }
        return catList
    }

    fun getPropertyData(response: JSONObject?, pageNo: Int): ArrayList<PropertyModel> {
        if (pageNo == 1)
            propertyList.clear()

        //   val array = JSONObject(response)
        var marry: JSONArray? = null
        try {
            marry = response?.get("results") as JSONArray
            propertyList.addAll(GsonBuilder().serializeNulls().create().fromJson(marry.toString(), propertyType))
        } catch (e: Exception) {
            Log.d("error", e.toString())
        }
        return propertyList
    }

    fun getFavPropertyData(response: JSONObject?, pageNo: Int): ArrayList<MyFavModel> {
        if (pageNo == 1)
            favModel.clear()

        //   val array = JSONObject(response)
        var marry: JSONArray? = null
        try {
            marry = response?.get("results") as JSONArray
            favModel.addAll(GsonBuilder().serializeNulls().create().fromJson(marry.toString(), favType))
        } catch (e: Exception) {
            Log.d("error", e.toString())
        }
        return favModel
    }
}
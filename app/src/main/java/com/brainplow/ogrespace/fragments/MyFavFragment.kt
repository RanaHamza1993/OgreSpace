package com.brainplow.ogrespace.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.StringRequest
import com.android.volley.toolbox.Volley
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.adapters.PropertyAdapter
import com.brainplow.ogrespace.apputils.Urls.urlGetFav
import com.brainplow.ogrespace.enums.LayoutType
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.models.PropertyModel
import com.facebook.share.Share
import org.json.JSONObject

class MyFavFragment : Fragment() {
    var propertyList: ArrayList<PropertyModel>? = null
    var recycle_states_more: RecyclerView? = null
    var layoutManager: LinearLayoutManager? = null
    var propertyAdapter: PropertyAdapter? = null
    var sharedPreferences: SharedPreferences? = null
    var token: String? = null


    override fun onResume() {
        super.onResume()
        acBarListener?.actionBarListener("Favourites")
        acBarListener?.isBackButtonEnabled(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mcontext = context
        acBarListener = context as Communicator.IActionBar
    }

    var acBarListener: Communicator.IActionBar? = null
    var mcontext: Context? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_states_more, container, false)
        findViews(view)
        createObjects()
        getData()

        return view
    }

    private fun getData() {
        propertyList?.clear()
        var request = object : StringRequest(Request.Method.GET, urlGetFav, Response.Listener { response ->

            var baseObj = JSONObject(response)
            var result = baseObj.getJSONArray("rersults")
            for (i in 0 until result!!.length()) {
                var propertyModel = PropertyModel()

                var innerObj: JSONObject = result.getJSONObject(i)
                var id = innerObj.getInt("id")
                propertyModel.id = id

                var created_at = innerObj.getString("created_at")
                propertyModel.created_at = created_at

                var user = innerObj.getInt("user")
                propertyModel.user = user

                var Property_id = innerObj.getJSONObject("Property_id")
                var pId = Property_id.getInt("id")
                propertyModel.property_id = pId

                var property_title = Property_id.getString("property_title")
                propertyModel.property_title = property_title

                var property_type = Property_id.getString("property_type")
                propertyModel.property_type = property_type

                var one_pic = Property_id.getString("one_pic")
                propertyModel.one_pic = one_pic

                var price = Property_id.getDouble("price")
                propertyModel.price = price

                var property_area = Property_id.getDouble("property_area")
                propertyModel.property_area = property_area

                var address = Property_id.getString("address")
                propertyModel.address = address

                var post_type = Property_id.getString("post_type")
                propertyModel.post_type = post_type

                var latitude = Property_id.getDouble("latitude")
                propertyModel.latitude = latitude

                var longitude = Property_id.getDouble("longitude")
                propertyModel.longitude = longitude

                propertyList?.add(propertyModel)
                propertyAdapter = PropertyAdapter(activity, propertyList!!, LayoutType.LayoutProperties, "delete")

                recycle_states_more!!.layoutManager = layoutManager
                recycle_states_more!!.adapter = propertyAdapter
            }


        }, Response.ErrorListener {


        }) {
            override fun getHeaders(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map.put("Authorization", "JWT "+token)
                return map
            }
        }
        var queue = Volley.newRequestQueue(activity)
        queue?.add(request)
    }

    private fun createObjects() {

        sharedPreferences = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
        token = sharedPreferences!!.getString("token", "JWT")

        propertyList = ArrayList()
        layoutManager = LinearLayoutManager(activity)


    }

    private fun findViews(view: View?) {
        recycle_states_more = view?.findViewById(R.id.recycle_states_more)

    }
}
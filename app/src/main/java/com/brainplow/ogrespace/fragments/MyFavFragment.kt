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
import com.android.volley.error.VolleyError
import com.android.volley.request.StringRequest
import com.android.volley.toolbox.Volley
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.activities.MainActivity
import com.brainplow.ogrespace.adapters.PropertyAdapter
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.apputils.Urls.urlAddToFav
import com.brainplow.ogrespace.apputils.Urls.urlGetFav
import com.brainplow.ogrespace.baseclasses.BaseFragment
import com.brainplow.ogrespace.baseclasses.PropertyBaseFragment
import com.brainplow.ogrespace.enums.LayoutType
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.extesnions.showErrorMessage
import com.brainplow.ogrespace.extesnions.showInfoMessage
import com.brainplow.ogrespace.extesnions.showSuccessMessage
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.VolleyParsing
import com.brainplow.ogrespace.kotlin.VolleyService
import com.brainplow.ogrespace.models.MyFavModel
import com.brainplow.ogrespace.models.PropertyModel
import com.facebook.share.Share
import org.json.JSONArray
import org.json.JSONObject

class MyFavFragment : PropertyBaseFragment(), Communicator.IVolleResult {


    override fun notifySuccess(requestType: RequestType?, response: String?, url: String, netWorkResponse: Int?) {

        if (url.equals(urlGetFav)) {
            //  val response = JSONObject(response)
            parseData(JSONObject(response))
            // setSalePropertyAdapter(volleyParsing?.getFavPropertyData(response, 1)!!)
        } else if (url.contains(Urls.urlDelFav, true)) {
            MainActivity.favItemsMap.remove(favId.toString())
            context?.showInfoMessage("Item deleted from favourite successfully")
        }
    }

    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {
        if (url.equals(urlAddToFav)) {
            context?.showSuccessMessage("Item not added to favourite")
        } else if (url.equals(Urls.urlDelFav)) {
            context?.showErrorMessage("Item not deleted from favourite")
        }
    }

    var propertyList: ArrayList<PropertyModel>? = ArrayList<PropertyModel>()
    var recycle_states_more: RecyclerView? = null
    var layoutManager: LinearLayoutManager? = null
    var propertyAdapter: PropertyAdapter? = null
    var sharedPreferences: SharedPreferences? = null
    var volleyService: VolleyService? = null
    var volleyParsing: VolleyParsing? = null

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
        super.setIVolleyResult(this)
        val view = inflater.inflate(R.layout.fragment_states_more, container, false)
        findViews(view)
        createObjects()
        getData()

        return view
    }

    private fun getData() {
        propertyList?.clear()
        volleyService?.getDataVolley(RequestType.StringRequest, Urls.urlGetFav, token!!)
    }

    private fun createObjects() {

        sharedPreferences = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
        token = sharedPreferences!!.getString("token", "JWT")
        recycle_states_more?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private fun findViews(view: View?) {
        volleyParsing = VolleyParsing()
        volleyService = VolleyService(this, mcontext!!.applicationContext)
        recycle_states_more = view?.findViewById(R.id.recycle_states_more)

    }

    private fun parseData(response: JSONObject?) {

        val array = response?.getJSONArray("results")
        try {
            for (i in 0 until array!!.length()) {

                var jsonObject = array.getJSONObject(i)
                val Id = jsonObject?.getString("id")
                jsonObject = jsonObject?.getJSONObject("Property_id")
                val Address = jsonObject?.getString("address")
                val property_id = jsonObject?.getString("id")
                val title = jsonObject?.getString("property_title")
                val type = jsonObject?.getString("property_type")
                val pic = jsonObject?.getString("one_pic")
                val Price = jsonObject?.getString("price")
                val area = jsonObject?.getString("property_area")
                val postType = jsonObject?.getString("post_type")
                val lat = jsonObject?.getString("latitude")
                val long = jsonObject?.getString("longitude")

                val favourite = PropertyModel(
                    address = Address,
                    id = property_id?.toInt(),
                    property_id = id?.toInt(),
                    property_title = title,
                    property_type = type,
                    one_pic = pic,
                    price = Price,
                    property_area = area?.toDouble(),
                    post_type = postType,
                    latitude = lat?.toDouble(),
                    longitude = long?.toDouble()
                )

                propertyList?.add(favourite)
            }
            setSalePropertyAdapter(propertyList!!)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setSalePropertyAdapter(propertyList: ArrayList<PropertyModel>) {
        val propertyAdapter = PropertyAdapter(context, propertyList, LayoutType.LayoutFavourites)
        propertyAdapter.run {
            setFavouriteListener(this@MyFavFragment)
            setItemClickListener(this@MyFavFragment)
        }
        recycle_states_more?.adapter = propertyAdapter
    }
}
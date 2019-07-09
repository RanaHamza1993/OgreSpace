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
import com.brainplow.ogrespace.adapters.PropertyAdapter
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.apputils.Urls.urlGetFav
import com.brainplow.ogrespace.baseclasses.BaseFragment
import com.brainplow.ogrespace.enums.LayoutType
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.VolleyParsing
import com.brainplow.ogrespace.kotlin.VolleyService
import com.brainplow.ogrespace.models.PropertyModel
import com.facebook.share.Share
import org.json.JSONObject

class MyFavFragment : BaseFragment(),Communicator.IVolleResult {

    override fun notifySuccess(requestType: RequestType?, response: String?, url: String, netWorkResponse: Int?) {
        val response=JSONObject(response)
        setSalePropertyAdapter(volleyParsing?.getPropertyData(response,1)!!)
    }
    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {

    }
    var propertyList: ArrayList<PropertyModel>? = null
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
        var view = inflater.inflate(R.layout.fragment_states_more, container, false)
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

        propertyList = ArrayList()
        layoutManager = LinearLayoutManager(activity)


    }

    private fun findViews(view: View?) {
        volleyParsing = VolleyParsing()
        volleyService = VolleyService(this, mcontext!!.applicationContext)
        recycle_states_more = view?.findViewById(R.id.recycle_states_more)

    }

    private fun setSalePropertyAdapter(propertyList: ArrayList<PropertyModel>) {
        val propertyAdapter = PropertyAdapter(context, propertyList, LayoutType.LayoutHorizontalProperties)
        recycle_states_more?.adapter = propertyAdapter
    }
}
package com.brainplow.ogrespace.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.error.VolleyError

import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.adapters.PropertyAdapter
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.baseclasses.BaseFragment
import com.brainplow.ogrespace.baseclasses.PropertyBaseFragment
import com.brainplow.ogrespace.enums.LayoutType
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.extesnions.showInfoMessage
import com.brainplow.ogrespace.extesnions.showSuccessMessage
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.LoadingDialog
import com.brainplow.ogrespace.kotlin.VolleyParsing
import com.brainplow.ogrespace.kotlin.VolleyService
import com.brainplow.ogrespace.models.PropertyModel
import org.json.JSONObject
import java.lang.Exception


class PropertiesMoreFragment : PropertyBaseFragment(), Communicator.IVolleResult, Communicator.IFavourites,Communicator.IItemDetail {

    override fun notifySuccess(requestType: RequestType?, response: JSONObject?, url: String, netWorkResponse: Int?) {
        // if (url.contains(Urls.urlPropertyByState)) {
        if (url.equals(Urls.urlAddToFav)) {
            if (netWorkResponse == 200)
                context?.showSuccessMessage("Item added to favourite successfully")
            else if (netWorkResponse == 202)
                deleteFromFav(favId)
        } else {
            if (pages == 1) {
                try {
                    bidItems = response!!.getInt("totalItems")
                    bidPages = response.getInt("totalPages")
                } catch (e: Exception) {
                }
            }
            flag = 1
            //      val list=volleyParsing?.getPropertyData(response,pages)
            setPropertyAdapter(volleyParsing?.getPropertyData(response, pages)!!)
        }


        //}
    }


    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {
        if (url.contains(Urls.urlPropertyByState)) {
            showErrorBody(error)
        }
    }

    override fun notifySuccess(requestType: RequestType?, response: String?, url: String, netWorkResponse: Int?) {
        if (url.contains(Urls.urlDelFav, true)) {
            context?.showInfoMessage("Item deleted from favourite successfully")
        }
    }

    var bidPages = 0
    var bidItems = 0
    var pages = 1
    var acBarListener: Communicator.IActionBar? = null
    var mcontext: Context? = null
    var volleyService: VolleyService? = null
    var volleyParsing: VolleyParsing? = null
    var layoutmanger: LinearLayoutManager? = null
    var propertyAdapter: PropertyAdapter? = null
    var flag = 0
    var mflag: Int? = null
    lateinit var load: LoadingDialog
    lateinit var recycleProperty: RecyclerView
    var stateName: String? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mcontext = context
        acBarListener = context as Communicator.IActionBar

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.setIVolleyResult(this)
        val view = inflater.inflate(R.layout.fragment_states_more, container, false)
        val bundle = arguments
        stateName = bundle?.getString("stateName")
        mflag = bundle?.getInt("mflag")
        setIds(view)
        setListeners()
        volleyRequests()
        return view
    }

    override fun onResume() {
        super.onResume()
        acBarListener?.run {
            isBackButtonEnabled(true)
            toolbarBackground(false)
            if (mflag == 1)
                actionBarListener("${stateName} Properties")
            else if (mflag == 2)
                actionBarListener("Sale Properties")
            else if (mflag == 3)
                actionBarListener("Lease Properties")
        }
    }

    fun setIds(view: View) {
        var bidPages = 0
        var bidItems = 0
        var pages = 1
        load = LoadingDialog("Loading", context)
        volleyParsing = VolleyParsing()
        volleyService = VolleyService(this, mcontext!!.applicationContext)
        recycleProperty = view.findViewById(R.id.recycle_states_more)
        layoutmanger = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycleProperty.layoutManager = layoutmanger
    }

    fun volleyRequests() {
        if (mflag == 1)
            volleyService?.getDataVolley(
                RequestType.JsonObjectRequest,
                Urls.urlPropertyByState + stateName + "/" + "?page=" + pages,
                ""
            )
        else if (mflag == 2)
            volleyService?.getDataVolley(
                RequestType.JsonObjectRequest,
                Urls.urlGetSaleProperties + "?page=" + pages,
                ""
            )
        else if (mflag == 3)
            volleyService?.getDataVolley(
                RequestType.JsonObjectRequest,
                Urls.urlGetLeaseProperties + "?page=" + pages,
                ""
            )
    }

    fun setListeners() {

    }

    fun setPropertyAdapter(propertyList: ArrayList<PropertyModel>) {

        if (pages == 1) {
            propertyAdapter = PropertyAdapter(context, propertyList, LayoutType.LayoutProperties)
            recycleProperty.adapter = propertyAdapter
        }

        recycleProperty.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutmanger!!.childCount
                val totalItemCount = propertyAdapter?.itemCount
                val pastVisibleItems = layoutmanger!!.findFirstVisibleItemPosition()
                if (pastVisibleItems + visibleItemCount >= totalItemCount!!) {
                    if ((bidPages != pages) && (flag == 1)) {
                        pages++
                        if (!load.ishowingg())
                            load.showdialog()

                        if (mflag == 1)
                            volleyService?.getDataVolley(
                                RequestType.JsonObjectRequest,
                                Urls.urlPropertyByState + stateName + "/" + "?page=" + pages,
                                ""
                            )
                        else if (mflag == 2)
                            volleyService?.getDataVolley(
                                RequestType.JsonObjectRequest,
                                Urls.urlGetSaleProperties + "?page=" + pages,
                                ""
                            )
                        else if (mflag == 3)
                            volleyService?.getDataVolley(
                                RequestType.JsonObjectRequest,
                                Urls.urlGetLeaseProperties + "?page=" + pages,
                                ""
                            )

                        flag = 0
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        })

        propertyAdapter?.run {
            setFavouriteListener(this@PropertiesMoreFragment)
            setItemClickListener(this@PropertiesMoreFragment)
            notifyDataSetChanged()
        }
        if (load.ishowingg())
            load.dismisss()
    }

}

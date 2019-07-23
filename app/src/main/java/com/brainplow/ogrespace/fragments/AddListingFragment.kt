package com.brainplow.ogrespace.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.error.VolleyError
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.activities.MainActivity
import com.brainplow.ogrespace.adapters.SearchKeyWordsAdapter
import com.brainplow.ogrespace.adapters.ServiceAdapter
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.baseclasses.BaseFragment
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.extesnions.showErrorMessage
import com.brainplow.ogrespace.extesnions.showInfoMessage
import com.brainplow.ogrespace.extesnions.showSuccessMessage
import com.brainplow.ogrespace.fragments.PaymentMethodFragment.Companion.token
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.VolleyParsing
import com.brainplow.ogrespace.kotlin.VolleyService
import com.brainplow.ogrespace.models.StateModel
import org.json.JSONArray
import org.json.JSONObject

class AddListingFragment : BaseFragment(), Communicator.IVolleResult {

    var et_p_name: EditText? = null
    var et_p_address: EditText? = null
    var et_p_holder: EditText? = null
    var et_p_company: EditText? = null
    var et_description: EditText? = null
    var et_contact: EditText? = null
    var et_p_area: EditText? = null
    var et_p_price: EditText? = null
    var crd_address_inner: CardView? = null

    companion object {
        var et_space_services: EditText? = null
    }


    var s_space_type: Spinner? = null
    var s_property_type: Spinner? = null
    var s_p_type: Spinner? = null

    var services_recycler: RecyclerView? = null


    var b_address_back: TextView? = null
    var b_address_next: TextView? = null


    var b_next: TextView? = null
    var crd_address: View? = null

    var crd_name: CardView? = null

    var sSpaceAdapter: ArrayAdapter<String>? = null
    var sPropertyAdapter: ArrayAdapter<String>? = null
    var sPriceTypeAdapter: ArrayAdapter<String>? = null

    var sPriceTypeArray: ArrayList<String>? = null
    var sTypeArray: ArrayList<String>? = null
    var sPropertyArray: ArrayList<String>? = null
    var sServiceArray: ArrayList<StateModel>? = null

    var volleyService: VolleyService? = null
    var volleyParsing: VolleyParsing? = null

    var sharedPreferences: SharedPreferences? = null

    var acBarListener: Communicator.IActionBar? = null
    var mcontext: Context? = null

    var serviceAdapter: ServiceAdapter? = null
    var isRecyclerVisible: Boolean? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mcontext = context
        acBarListener = context as Communicator.IActionBar
    }

    override fun onResume() {
        super.onResume()
        acBarListener?.actionBarListener("Add List")
        acBarListener?.isBackButtonEnabled(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_listing, container, false)

        setIds(view)
        createObjects()
        populateArrays()
        setAdapters()
        getSevicesData()
        setClicks()
        return view
    }

    private fun setClicks() {
        b_next?.setOnClickListener(View.OnClickListener {


            if (et_p_name?.text.toString().length == 0) {
                    et_p_name?.setError("Please enter Property Name")
            } else if (et_p_address?.text.toString().length == 0) {
                et_p_address?.setError("Please enter Property Address")
            } else if (et_p_holder?.text.toString().length == 0) {
                et_p_holder?.setError("Please enter Property Holder's Name")
            } else if (et_p_company?.text.toString().length == 0) {
                et_p_company?.setError("Please enter Presenting Company's Name")
            } else{
                crd_address?.visibility = View.VISIBLE
                crd_name?.visibility = View.GONE
            }
        })

        b_address_back?.setOnClickListener(View.OnClickListener {
            crd_address?.visibility = View.GONE
            crd_name?.visibility = View.VISIBLE
        })

        et_space_services?.setOnClickListener(View.OnClickListener {
            if (isRecyclerVisible!!) {
                services_recycler?.visibility = View.GONE
                isRecyclerVisible = false
                et_space_services?.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_keyboard_arrow_right_black_24dp,
                    0
                );
            } else {
                services_recycler?.visibility = View.VISIBLE
                isRecyclerVisible = true
                et_space_services?.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_keyboard_arrow_down_black_24dp,
                    0
                );
            }


        })

        crd_address_inner?.setOnClickListener(View.OnClickListener {
            if (isRecyclerVisible!!) {
                services_recycler?.visibility = View.GONE
                isRecyclerVisible = false
                et_space_services?.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_keyboard_arrow_right_black_24dp,
                    0
                );
            }
        })
    }

    private fun createObjects() {

        volleyParsing = VolleyParsing()
        volleyService = VolleyService(this, mcontext!!.applicationContext)
        //set adapter and linear layout manager

        sharedPreferences = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
        token = sharedPreferences!!.getString("token", "JWT")


    }

    private fun getSevicesData() {
        sServiceArray?.clear()
        volleyService?.getDataVolley(RequestType.StringRequest, Urls.urlGetServices, "")
    }

    private fun populateArrays() {
        sTypeArray = ArrayList()
        sTypeArray?.add("Lease")
        sTypeArray?.add("Sale")
        sSpaceAdapter?.notifyDataSetChanged()


        sPriceTypeArray = ArrayList()
        sPriceTypeArray?.add("Per Month")
        sPriceTypeArray?.add("Per Year")
        sPriceTypeAdapter?.notifyDataSetChanged()


        sPropertyArray = ArrayList()
        sPropertyArray?.add("Offices")
        sPropertyArray?.add("Retail")
        sPropertyArray?.add("Co-working")
        sPropertyArray?.add("Land")
        sPropertyArray?.add("Industrial")
        sPropertyArray?.add("Medical Offices")

        sPropertyAdapter?.notifyDataSetChanged()

        sServiceArray = ArrayList()

    }

    private fun setAdapters() {
        sSpaceAdapter = object : ArrayAdapter<String>(
            activity,
            android.R.layout.simple_spinner_item, sTypeArray
        ) {

        }
        s_space_type?.setAdapter(sSpaceAdapter)
        sSpaceAdapter?.setDropDownViewResource(R.layout.spinner_item_layout)




        sPropertyAdapter = object : ArrayAdapter<String>(
            activity,
            android.R.layout.simple_spinner_item, sPropertyArray
        ) {

        }
        s_property_type?.setAdapter(sPropertyAdapter)
        sPropertyAdapter?.setDropDownViewResource(R.layout.spinner_item_layout)



        sPriceTypeAdapter = object : ArrayAdapter<String>(
            activity,
            android.R.layout.simple_spinner_item, sPriceTypeArray
        ) {

        }
        s_p_type?.setAdapter(sPriceTypeAdapter)
        sPriceTypeAdapter?.setDropDownViewResource(R.layout.spinner_item_layout)


    }

    private fun setIds(view: View?) {


        et_p_name = view?.findViewById(R.id.et_p_name)
        et_p_address = view?.findViewById(R.id.et_p_address)
        et_p_holder = view?.findViewById(R.id.et_p_holder)
        et_p_company = view?.findViewById(R.id.et_p_company)
        s_p_type = view?.findViewById(R.id.s_p_type)
        et_description = view?.findViewById(R.id.et_description)
        et_contact = view?.findViewById(R.id.et_contact)
        et_p_area = view?.findViewById(R.id.et_p_area)
        et_p_price = view?.findViewById(R.id.et_p_price)

        crd_address_inner = view?.findViewById(R.id.crd_address_inner)

        services_recycler = view?.findViewById(R.id.services_recycler)
        services_recycler?.visibility = View.GONE
        isRecyclerVisible = false

        s_space_type = view?.findViewById(R.id.s_space_type)
        s_property_type = view?.findViewById(R.id.s_property_type)
        et_space_services = view?.findViewById(R.id.et_space_services)
        et_space_services?.setCompoundDrawablesWithIntrinsicBounds(
            0,
            0,
            R.drawable.ic_keyboard_arrow_right_black_24dp,
            0
        );

        b_next = view?.findViewById(R.id.b_next)


        b_address_next = view?.findViewById(R.id.b_address_next)
        b_address_back = view?.findViewById(R.id.b_address_back)

        crd_address = view?.findViewById(R.id.crd_address)
        crd_name = view?.findViewById(R.id.crd_name)
    }

    override fun notifySuccess(requestType: RequestType?, response: String?, url: String, netWorkResponse: Int?) {

        if (url.equals(Urls.urlGetServices)) {

            parseData(JSONArray(response))

        }
    }

    private fun parseData(jsonArray: JSONArray) {
        for (i in 0 until jsonArray?.length()!!) {
            var stateModel = StateModel()
            var baseObj: JSONObject = jsonArray?.getJSONObject(i)
            var our_services = baseObj?.getString("our_services")
            var logo_of_services_32px = baseObj?.getString("logo_of_services_32px")

            stateModel?.state = our_services
            stateModel?.icon_image = logo_of_services_32px
            stateModel?.check_value = false

            sServiceArray?.add(stateModel)


        }
        var manager: LinearLayoutManager = LinearLayoutManager(activity)
        serviceAdapter = ServiceAdapter(activity!!, sServiceArray!!)
        services_recycler!!.layoutManager = manager
        services_recycler!!.adapter = serviceAdapter
        sSpaceAdapter?.notifyDataSetChanged()
    }

    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {
        if (url.equals(Urls.urlGetServices)) {
        }
    }
}
package com.brainplow.ogrespace.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.error.VolleyError
import com.android.volley.request.JsonObjectRequest
import com.android.volley.request.StringRequest

import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.apputils.Urls.urlContactUs
import com.brainplow.ogrespace.baseclasses.BaseFragment
import com.brainplow.ogrespace.constants.StaticFunctions
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.extesnions.showErrorMessage
import com.brainplow.ogrespace.extesnions.showSuccessMessage
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.JWTUtils
import com.brainplow.ogrespace.kotlin.MySingleton
import com.brainplow.ogrespace.kotlin.SSL
import com.brainplow.ogrespace.kotlin.VolleyService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import es.dmoral.toasty.Toasty
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class ContactUsFragment : BaseFragment(),Communicator.IVolleResult {

    override fun notifySuccess(requestType: RequestType?, response: JSONObject?, url: String, netWorkResponse: Int?) {
        if(url.equals(urlContactUs)){
            context?.showSuccessMessage("Your feedback is successfully sent")
        }
    }

    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {
        if(url.equals(urlContactUs)){
            context?.showErrorMessage("Your feedback is not successfully sent")
        }
        showErrorBody(error)
    }
    var mapFragment: SupportMapFragment?=null
    private lateinit var mMap: GoogleMap
    private lateinit var contactusurl: String
    // lateinit var bckbtn:ImageView
    // private lateinit var stringRequest: JsonObjectRequest
    val MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;

    var acBarListener: Communicator.IActionBar? = null
    var name: AppCompatEditText? = null
    var email: AppCompatEditText? = null
    var phoneno: AppCompatEditText? = null
    var subject: AppCompatEditText? = null
    var message: AppCompatEditText? = null
    var submit: TextView? = null
    var userid: String? = null
    var decode: String? = null
    var token: String? = null
    var username: String? = null
    var userEmail: String? = null
    var mcontext: Context? = null
    var volleyService: VolleyService? = null
    lateinit var navigationView: NavigationView
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mcontext = context
        acBarListener = context as Communicator.IActionBar
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contact_us, container, false)
        setTopBar()
        setIds(view)
        setClicks()

        return view

    }




    private fun setTopBar() {
        acBarListener?.actionBarListener("Contact Us")
        acBarListener?.isBackButtonEnabled(true)
    }

    private fun setClicks() {

        if (mapFragment == null) {
            val fm = fragmentManager
            val ft = fm?.beginTransaction()
            mapFragment = SupportMapFragment.newInstance()
            ft?.replace(R.id.mappp, mapFragment!!)?.commit()
            mapFragment = childFragmentManager.findFragmentById(R.id.mappp) as SupportMapFragment?
            if (mapFragment == null) {
                val fm = fragmentManager
                val ft = fm?.beginTransaction()
                mapFragment = SupportMapFragment.newInstance()
                ft?.replace(R.id.mappp, mapFragment!!)?.commit()

            }


            mapFragment!!.getMapAsync(object : OnMapReadyCallback {
                override fun onMapReady(googleMap: GoogleMap?) {


                    googleMap?.getUiSettings()?.setAllGesturesEnabled(true)
                    val sydney = LatLng(32.9480465, -96.8270723)
                    googleMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Dallas"))
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))
                }
            })

        }
        submit?.setOnClickListener {
            //
            val isValidated=checkValidation()
            if(isValidated)
                contactusApi()
        }
    }


    fun setIds(view:View){
        volleyService = VolleyService(this, mcontext!!.applicationContext)
        mapFragment = childFragmentManager.findFragmentById(R.id.mappp) as SupportMapFragment?
        name = view.findViewById(R.id.contact_name)
        email = view.findViewById(R.id.contact_email)
        phoneno = view.findViewById(R.id.contact_number)
        subject = view.findViewById(R.id.contact_subject)
        message = view.findViewById(R.id.contact_message)
        submit = view.findViewById(R.id.contact_submit)
        navigationView = activity!!.findViewById(R.id.nav_view)
    }




    fun checkValidation():Boolean {
        val namee = name?.text?.toString()
        val emaill = email?.text?.toString()
        val no = phoneno?.text?.toString()
        val companyName = subject?.text?.toString()
        val messg = message?.text?.toString()
        val isValid = StaticFunctions.emailValidator(emaill)
        if (namee!!.isEmpty()) {

            context?.showErrorMessage("Enter your name")
            return false
        } else if (emaill!!.isEmpty()) {
            context?.showErrorMessage("Enter valid email address")
            return false
        } else if (!isValid) {
            context?.showErrorMessage("Enter valid email address")
            return false
        } else if (no!!.isEmpty()) {

            context?.showErrorMessage("Enter your phone number")
            return false
        } else if (companyName!!.isEmpty()) {
            context?.showErrorMessage("Enter your company name")
            return false
        } else if (messg!!.isEmpty()) {
            context?.showErrorMessage("Enter your message")
        }
        return true

    }


    override fun onResume() {
        super.onResume()
        navigationView.menu.findItem(R.id.established_contact).setChecked(true)
        acBarListener?.run{
            actionBarListener("Contact Us")
            isBackButtonEnabled(true)
            toolbarBackground(false)
        }
    }

    override fun onPause() {
        super.onPause()
        navigationView.menu.findItem(R.id.established_contact).setChecked(false)
    }
    private fun contactusApi() {
        val name = name?.text.toString()
        val email = email?.text.toString()
        val phoneNo = phoneno?.text.toString()
        val subjct = subject?.text.toString()
        val message = message?.text.toString()
        var statusCode = 0;
        SSL.sslCertificates()
        val rootObject = JSONObject()
        rootObject.put("Name", name.toString())
        rootObject.put("Email", email.toString())
        rootObject.put("Telephone", phoneNo.toString())
        rootObject.put("CompanyName", subjct.toString())
        rootObject.put("Message", message.toString())
        volleyService?.postDataVolley(RequestType.JsonObjectRequest, Urls.urlContactUs, rootObject, "")
    }


}

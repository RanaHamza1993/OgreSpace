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
import com.brainplow.ogrespace.baseclasses.BaseFragment
import com.brainplow.ogrespace.constants.StaticFunctions
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.JWTUtils
import com.brainplow.ogrespace.kotlin.MySingleton
import com.brainplow.ogrespace.kotlin.SSL
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

class ContactUs : BaseFragment() {

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

//        val sharedPreferences = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
//        token = sharedPreferences?.getString("token", "")//"No name defined" is the default value.
//        try {
//            decode = JWTUtils.decoded(token)
//        } catch (e: Exception) {
//            e.printStackTrace()
//
//        }
//        if (decode != null) {
//            val decodejson = JSONObject(decode)
//            try {
//                userid = decodejson.getString("user_id")
//                username = decodejson.getString("username")
//                userEmail = decodejson.getString("email")
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        getUserDetail()
        setTopBar()
        findViews(view)
        setClicks()


        navigationView = activity!!.findViewById(R.id.nav_view)
        acBarListener?.actionBarListener("Contact Us")
        acBarListener?.isBackButtonEnabled(true)

        name = view?.findViewById(R.id.contact_name)
        email = view?.findViewById(R.id.contact_email)
        phoneno = view?.findViewById(R.id.contact_number)
        subject = view?.findViewById(R.id.contact_subject)
        message = view?.findViewById(R.id.contact_message)
        submit = view?.findViewById(R.id.contact_submit)
//        var mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//
//
//        //Toast.makeText(context, "Map loading", Toast.LENGTH_LONG).show();
//
//        Log.d("map", "loading");
//        mapFragment.getMapAsync(this)

        mapFragment = childFragmentManager?.findFragmentById(R.id.mappp) as SupportMapFragment?
        if (mapFragment == null) {
            val fm = fragmentManager
            val ft = fm?.beginTransaction()
            mapFragment = SupportMapFragment.newInstance()
            ft?.replace(R.id.mappp, mapFragment!!)?.commit()
            mapFragment = childFragmentManager?.findFragmentById(R.id.mappp) as SupportMapFragment?
            if (mapFragment == null) {
                val fm = fragmentManager
                val ft = fm?.beginTransaction()
                mapFragment = SupportMapFragment.newInstance()
                ft?.replace(R.id.mappp, mapFragment!!)?.commit()

            }


            mapFragment!!.getMapAsync(object : OnMapReadyCallback {
                override fun onMapReady(googleMap: GoogleMap?) {


                    googleMap?.getUiSettings()?.setAllGesturesEnabled(true)
                    // googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
//                googleMap?.addMarker(MarkerOptions().position(LatLng(31.5162529, 74.3407591)).title("BrainPlow"))
//                var cam: CameraPosition? = null
//                cam = CameraPosition.builder().target(LatLng(31.5162529, 74.3407591)).zoom(18F).bearing(0F).tilt(45F).build()
//                googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cam))


                    val sydney = LatLng(32.9480465, -96.8270723)
                    googleMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Dallas"))
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))

                }


            })
            acBarListener?.actionBarListener("Contact Us")



        }
        return view

    }


    private fun setTopBar() {
        acBarListener?.actionBarListener("Contact Us")
        acBarListener?.isBackButtonEnabled(true)
    }

    private fun setClicks() {

        submit?.setOnClickListener {
            //
//            val isValidated=checkValidation()
//            if(isValidated)
//                contactusApi()
        }
    }


    private fun findViews(view: View) {
        name = view?.findViewById(R.id.contact_name)
        email = view?.findViewById(R.id.contact_email)
        phoneno = view?.findViewById(R.id.contact_number)
        subject = view?.findViewById(R.id.contact_subject)
        message = view?.findViewById(R.id.contact_message)
        submit = view?.findViewById(R.id.contact_submit)
        navigationView = activity!!.findViewById(R.id.nav_view)
    }




    fun checkValidation():Boolean {


        val namee = name?.text?.toString()
        val emaill = email?.text?.toString()
        val no = phoneno?.text?.toString()
        val sbjct = subject?.text?.toString()
        val messg = message?.text?.toString()
        val isValid = StaticFunctions.emailValidator(emaill)
        if (namee!!.isEmpty()) {

            name?.error = "Enter Your Name"
            return false
        } else if (emaill!!.isEmpty()) {
            email?.error = "Enter Valid Email Address"
            return false
        } else if (!isValid) {
            email?.error = "Enter Valid Email Address"
            return false
        } else if (no!!.isEmpty()) {

            phoneno?.error = "Enter Your PhoneNumber"
            return false
        } else if (sbjct!!.isEmpty()) {

            subject?.error = "Enter Your Subject"
            return false
        } else if (messg!!.isEmpty()) {
            val isValid = StaticFunctions.emailValidator(emaill)
            if (namee!!.isEmpty()) {

                name?.error = "Enter Your Name"
                return false
            } else if (emaill!!.isEmpty()) {
                email?.error = "Enter Valid Email Address"
                return false
            } else if (!isValid) {
                email?.error = "Enter Valid Email Address"
                return false
            } else if (no!!.isEmpty()) {

                phoneno?.error = "Enter Your PhoneNumber"
                return false
            } else if (sbjct!!.isEmpty()) {

                subject?.error = "Enter Your Subject"
                return false
            } else if (messg!!.isEmpty()) {

                message?.error = "Enter Your Message"
                return false
            }

        }
        return true

    }


    override fun onResume() {
        super.onResume()
        navigationView.menu.findItem(R.id.established_contact).setChecked(true)
        acBarListener?.isBackButtonEnabled(true)
        acBarListener?.toolbarBackground(false)
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
        rootObject.put("name", name.toString())
        rootObject.put("email", email.toString())
        rootObject.put("phone", phoneNo.toString())
        rootObject.put("subject", subjct)
        rootObject.put("message", message.toString())
        rootObject.put("name",name.toString())
        rootObject.put("email",email.toString())
        rootObject.put("phone",phoneNo.toString())
        rootObject.put("subject",subjct)
        rootObject.put("message",message.toString())

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, "", rootObject,
            Response.Listener { response ->
                try {
                    Toasty.info(
                        context!!,
                        "Your message has been received we will contact you soon",
                        Toast.LENGTH_LONG,
                        true
                    ).show()


                }

                catch (e:Exception)
                {
                    val a=5
                }
            }
            ,
            Response.ErrorListener { error ->

                Toasty.error(context!!, "" + error.toString(), Toast.LENGTH_LONG, true).show()
            }
        )
        {
//            override fun getParams(): MutableMap<String, String> {
//                val params = HashMap<String, String>()
//                try {
//                    params.put("name", name.toString())
//                    params.put("email", email.toString())
//                    params.put("phone", phoneNo.toString())
//                    params.put("subject", "subject")
//                    params.put("message", message.toString())
//
//                } catch (e: Exception) {
//                    Log.i("", "getParams: " + e)
//                }
//                return params
//            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                println(response.toString())
                return super.parseNetworkResponse(response)
            }


            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }
        }


        // val requestQue = Volley.newRequestQueue(context)
        //requestQue.add(jsonObjectRequest)

        MySingleton.getInstance(context!!).addToRequestQueue(jsonObjectRequest)

    }
    fun getUserDetail() {
        val stringRequest = object : StringRequest(Request.Method.GET, "", Response.Listener<String> { s ->
            try {
                val obj = JSONObject(s)
                username = obj.getString("firstname") + " " + obj.getString("lastname")
                name?.setText(username.toString())
                email?.setText(userEmail)

            } catch (e: JSONException) {

            }
        }, Response.ErrorListener { error: VolleyError? ->
            //            Log.e("error", "error")

        }) {
            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                println(response.toString())
                return super.parseNetworkResponse(response)
            }
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json; charset=utf-8"
                headers.put("Authorization", "JWT " + token)

                return headers
            }

            override fun getBodyContentType(): String {
                return "application/json"

            }
        }
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(mcontext!!.applicationContext).addToRequestQueue(stringRequest)
    }


}

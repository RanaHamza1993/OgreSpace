package com.brainplow.ogrespace.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.error.VolleyError
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.activities.FullScreenImageActivity
import com.brainplow.ogrespace.activities.MainActivity
import com.brainplow.ogrespace.adapters.PropertyAdapter
import com.brainplow.ogrespace.adapters.StatesAdapter
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.baseclasses.BaseFragment
import com.brainplow.ogrespace.baseclasses.PropertyBaseFragment
import com.brainplow.ogrespace.enums.LayoutType
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.extesnions.showInfoMessage
import com.brainplow.ogrespace.extesnions.showSuccessMessage
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.LoadingDialog
import com.brainplow.ogrespace.kotlin.SSL
import com.brainplow.ogrespace.kotlin.VolleyService
import com.brainplow.ogrespace.models.PropertyModel
import com.brainplow.ogrespace.models.StateModel
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.DefaultSliderView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class PropertyDetailFragment : PropertyBaseFragment(), Communicator.IVolleResult {

    override fun notifySuccess(requestType: RequestType?, response: JSONObject?, url: String, netWorkResponse: Int?) {
        if (url.equals(Urls.urlAddToFav)) {
            if (netWorkResponse == 200) {
                MainActivity.favItemsMap.put(favId.toString(), favId!!)
                context?.showSuccessMessage("Item added to favourite successfully")
            } else if (netWorkResponse == 202)
                deleteFromFav(favId)

        }
    }

    override fun notifySuccess(requestType: RequestType?, response: String?, url: String, netWorkResponse: Int?) {
        //    val a = 5
        if (url.contains(Urls.urlDelFav, true)) {
            MainActivity.favItemsMap.remove(favId.toString())
            context?.showInfoMessage("Item deleted from favourite successfully")
        } else if (url.contains(Urls.urlProperyDetail)) {
            getDetailItems(response)
        }

        laoding?.dismisss()
    }

    override fun notifySuccess(requestType: RequestType?, response: JSONArray?, url: String, netWorkResponse: Int?) {

        if (url.contains(Urls.urlSimilarProperties))
            getSimilarItems(response)
        laoding?.dismisss()

    }

    private fun getSimilarItems(response: JSONArray?) {
        propertyArrayList?.clear()
        for (i in 0 until response?.length()!!) {
            val propertyModel = PropertyModel()
            val baseObj = response.getJSONObject(i)
            val id = baseObj?.getInt("id")
            propertyModel.id = id

            val property_title = baseObj?.getString("property_title")
            propertyModel.property_title = property_title

            val property_type = baseObj?.getString("property_type")
            propertyModel.property_type = property_type

            val one_pic = baseObj?.getString("one_pic")
            propertyModel.one_pic = one_pic

            val price = baseObj?.getDouble("price")
            propertyModel.price = price

            val property_area = baseObj?.getDouble("property_area")
            propertyModel.property_area = property_area

            val address = baseObj?.getString("address")
            propertyModel.address = address

            val post_type = baseObj?.getString("post_type")
            propertyModel.post_type = post_type

            val latitude = baseObj?.getDouble("latitude")
            propertyModel.latitude = latitude

            val longitude = baseObj?.getDouble("longitude")
            propertyModel.longitude = longitude

            propertyArrayList?.add(propertyModel)
        }
        setAdapter(propertyArrayList!!)
    }

    private fun setAdapter(propertyArrayList: ArrayList<PropertyModel>) {
        linearLayoutManager = LinearLayoutManager(mcontext)
        propertyAdapter = PropertyAdapter(mcontext, propertyArrayList, LayoutType.LayoutProperties)
        recyc_similar_prop?.layoutManager = linearLayoutManager as RecyclerView.LayoutManager?
        propertyAdapter?.run {
            setFavouriteListener(this@PropertyDetailFragment)
            setItemClickListener(this@PropertyDetailFragment)
        }
        recyc_similar_prop?.adapter = propertyAdapter
        recyc_similar_prop?.setNestedScrollingEnabled(false)

    }

    private fun getDetailItems(response: String?) {
        featuresArray?.clear()
        val baseObj = JSONObject(response)

        val arrayPics = baseObj.getJSONArray("pics")
        for (i in 0 until arrayPics?.length()!!) {
            val picUrl: String = arrayPics.getString(i)
           // Log.d("propertPics", picUrl)
            url_maps.add(picUrl)
        }
        propertyImages()
        val arrayResults = baseObj.getJSONArray("results")
        var propertyLng: String? = null
        var propertyLat: String? = null
        var property_title: String? = null
        for (i in 0 until arrayResults?.length()!!) {
            val dataObj = arrayResults.getJSONObject(i)


            val mapped_servicesArray = dataObj?.getJSONArray("mapped_services")
            for (a in 0 until mapped_servicesArray?.length()!!) {
                val stateModel = StateModel()
                val featuresObj = mapped_servicesArray.getJSONObject(a)
                val our_services = featuresObj?.getString("our_services")
                stateModel.state = our_services

                val logo_of_services = featuresObj?.getString("logo_of_services_64px")
                stateModel.icon_image = logo_of_services

                val id = featuresObj?.getInt("id")
                stateModel.id = id

                featuresArray?.add(stateModel)
                gridLayoutManager = GridLayoutManager(activity, 5)
                statesAdapter = StatesAdapter(activity, featuresArray!!, LayoutType.LayoutStateSmall, "features")
                recyc_features?.layoutManager = gridLayoutManager as RecyclerView.LayoutManager?
                recyc_features?.adapter = statesAdapter
                recyc_features?.setNestedScrollingEnabled(true);
            }
            val id = dataObj.getString("id")
            val address = dataObj.getString("address")
            property_address_txt?.setText(address)
            property_location_txt?.setText(address)
            val property_type = dataObj.getString("property_type")
            property_type_txt?.setText(property_type)
            propertyLat = dataObj.getString("latitude")
            propertyLng = dataObj.getString("longitude")
            val pic_url = dataObj.getString("pic_url")
            val one_pic = dataObj.getString("one_pic")
            val property_id = dataObj.getString("property_id")
            property_id_txt?.setText(property_id)
            val description = dataObj.getString("description")
            property_dis_txt?.setText(description)
            val price = dataObj.getString("price")
            property_price_txt?.setText(price)
            property_pri_txt?.setText(price)
            var presented_company = dataObj.getString("presented_company")
            val presented_name = dataObj.getString("presented_name")
            property_presented_txt?.setText(presented_name)
            val state = dataObj.getString("state")
            val zipcode = dataObj.getString("zipcode")
            val city = dataObj.getString("city")
            val country = dataObj.getString("country")
            val posted_date = dataObj.getString("posted_date")
            val services = dataObj.getString("services")
            property_title = dataObj.getString("property_title")
            property_company_txt?.setText(property_title)
            val contact_no = dataObj.getString("contact_no")
            val active_bool = dataObj.getBoolean("active_bool")
            val property_area = dataObj.getString("property_area")
            property_area_txt?.setText(property_area)
            var price_type = dataObj.getString("price_type")
            var post_type = dataObj.getString("post_type")
        }
        setMap(propertyLat, propertyLng, property_title)
    }

    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {
        //  showErrorBody(error)
    }

    var propertyArrayList: ArrayList<PropertyModel>? = null
    lateinit var mDemoSlider: SliderLayout
    var statesAdapter: StatesAdapter? = null
    var propertyAdapter: PropertyAdapter? = null
    var gridLayoutManager: GridLayoutManager? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var featuresArray: ArrayList<StateModel>? = null
    var property_price_txt: TextView? = null
    var property_dis_txt: TextView? = null
    var property_address_txt: TextView? = null
    var property_id_txt: TextView? = null
    var property_type_txt: TextView? = null
    var property_pri_txt: TextView? = null
    var property_area_txt: TextView? = null
    var property_location_txt: TextView? = null
    var property_company_txt: TextView? = null
    var property_presented_txt: TextView? = null
    var recyc_features: RecyclerView? = null
    var recyc_similar_prop: RecyclerView? = null
    var acBarListener: Communicator.IActionBar? = null
    var mcontext: Context? = null
    var volleyService: VolleyService? = null
    var token: String? = null
    var mapFragment: SupportMapFragment? = null
    var itemId = 0
    var url_maps = ArrayList<String>()
    var laoding: LoadingDialog? = null
    var handler = Handler()


    override fun onPause() {

        handler.removeCallbacksAndMessages(null)
        if (laoding?.ishowingg()!!) {
            laoding?.dismisss()
        }
        super.onPause()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.setIVolleyResult(this)
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        val b = arguments
        SSL.sslCertificates()
        //url_maps = HashMap()

        itemId = b!!.getInt("id")
        setIds(view)
        handler.postDelayed({
            laoding?.showdialog()
            volleyRequests()
        }, 2000)


        return view
    }

    private fun setMap(propertyLat: String?, propertyLng: String?, property_title: String?) {
        try {
            mapFragment = childFragmentManager.findFragmentById(R.id.mappp) as SupportMapFragment?
        } catch (e: Exception) {
        }
        if (mapFragment == null) {
            val fm = fragmentManager
            val ft = fm?.beginTransaction()
            mapFragment = SupportMapFragment.newInstance()
            ft?.replace(R.id.mappp, mapFragment!!)?.commit()
            //    mapFragment = childFragmentManager.findFragmentById(R.id.mappp) as SupportMapFragment?


            mapFragment!!.getMapAsync(object : OnMapReadyCallback {
                override fun onMapReady(googleMap: GoogleMap?) {


                    googleMap?.getUiSettings()?.setAllGesturesEnabled(true)


                    val sydney = LatLng(propertyLat!!.toDouble(), propertyLng!!.toDouble())
                    googleMap?.addMarker(MarkerOptions().position(sydney).title(property_title))
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17.0f))


                }


            })
        }
    }

    private fun propertyImages() {


        for (i in 0 until   url_maps.size) {
            val textSliderView = DefaultSliderView(activity)
            // initialize a SliderLayout
            textSliderView.image(url_maps[i])
                .setScaleType(BaseSliderView.ScaleType.Fit)
                .setOnSliderClickListener {
                    val intent= Intent(mcontext, FullScreenImageActivity::class.java)
                    intent.putExtra("map",url_maps)
                    intent.putExtra("pos",i)
                    startActivity(intent)
                }

            mDemoSlider.run {
                setBackgroundColor(Color.TRANSPARENT)
                setPresetTransformer(SliderLayout.Transformer.Default)
                setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
                setCustomAnimation(DescriptionAnimation())
                setDuration(5000)
                textSliderView.bundle(Bundle())
                textSliderView.bundle
                    .putString("extra", url_maps[i])
                addSlider(textSliderView)
            }

            //add your extra information


        }
    }

    fun setIds(view: View) {
        volleyService = VolleyService(this, mcontext!!.applicationContext)

        property_price_txt = view.findViewById(R.id.property_price_txt);
        property_dis_txt = view.findViewById(R.id.property_dis_txt);
        property_address_txt = view.findViewById(R.id.property_address_txt);
        property_id_txt = view.findViewById(R.id.property_id_txt);
        property_type_txt = view.findViewById(R.id.property_type_txt);
        property_pri_txt = view.findViewById(R.id.property_pri_txt);
        property_area_txt = view.findViewById(R.id.property_area_txt);
        property_location_txt = view.findViewById(R.id.property_location_txt);
        property_company_txt = view.findViewById(R.id.property_company_txt);
        property_presented_txt = view.findViewById(R.id.property_presented_txt);
        recyc_features = view.findViewById(R.id.recyc_features);
        recyc_similar_prop = view.findViewById(R.id.recyc_similar_prop);
        featuresArray = ArrayList()
        propertyArrayList = ArrayList()
        mDemoSlider = view.findViewById(R.id.property_img);
        mDemoSlider.getPagerIndicator()
            .setDefaultIndicatorColor(getResources().getColor(R.color.Red), getResources().getColor(R.color.gray));

        laoding = LoadingDialog("", activity!!)
    }

    fun volleyRequests() {
        volleyService?.getDataVolley(RequestType.StringRequest, Urls.urlProperyDetail + itemId + "/", "")
        volleyService?.getDataVolley(RequestType.ArrayRequest, Urls.urlSimilarProperties + itemId + "/", "")
    }

    override fun onResume() {
        super.onResume()
        acBarListener?.actionBarListener("Property Details")
        acBarListener?.isBackButtonEnabled(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mcontext = context
        acBarListener = context as Communicator.IActionBar
    }
}
package com.brainplow.ogrespace.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Property
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.error.VolleyError
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.adapters.StatesAdapter
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.baseclasses.BaseFragment
import com.brainplow.ogrespace.enums.LayoutType
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.VolleyService
import com.brainplow.ogrespace.models.PropertyModel
import com.brainplow.ogrespace.models.StateModel
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.DefaultSliderView
import org.json.JSONObject
import java.util.HashMap

class PropertyDetailFragment : BaseFragment(), Communicator.IVolleResult {

    override fun notifySuccess(requestType: RequestType?, response: String?, url: String, netWorkResponse: Int?) {
        val a = 5
        featuresArray?.clear()
        var baseObj = JSONObject(response)
        var arrayResults = baseObj?.getJSONArray("results")
        for (i in 0 until arrayResults?.length()!!) {
            var dataObj = arrayResults?.getJSONObject(i)

            var mapped_servicesArray = dataObj?.getJSONArray("mapped_services")
            for (i in 0 until mapped_servicesArray?.length()!!) {
                var stateModel = StateModel()
                var featuresObj = mapped_servicesArray?.getJSONObject(i)
                var our_services = featuresObj?.getString("our_services")
                stateModel?.state = our_services

                var logo_of_services = featuresObj?.getString("logo_of_services_30px")
                stateModel?.icon_image = logo_of_services

                var id = featuresObj?.getInt("id")
                stateModel?.id = id

                featuresArray?.add(stateModel)
                recyc_features?.setLayoutManager(GridLayoutManager(activity, 5));
                statesAdapter = StatesAdapter(activity, featuresArray!!, LayoutType.LayoutStatesCat, "features")
                recyc_features?.adapter = statesAdapter
                recyc_features?.setNestedScrollingEnabled(true);
            }

            var id = dataObj?.getString("id")

            var address = dataObj?.getString("address")
            property_address_txt?.setText(address)
            property_location_txt?.setText(address)

            var property_type = dataObj?.getString("property_type")
            property_type_txt?.setText(property_type)

            var latitude = dataObj?.getString("latitude")
            var longitude = dataObj?.getString("longitude")
            var pic_url = dataObj?.getString("pic_url")

            var one_pic = dataObj?.getString("one_pic")

            var property_id = dataObj?.getString("property_id")
            property_id_txt?.setText(property_id)

            var description = dataObj?.getString("description")
            property_dis_txt?.setText(description)

            var price = dataObj?.getString("price")
            property_price_txt?.setText(price)
            property_pri_txt?.setText(price)

            var presented_company = dataObj?.getString("presented_company")
            property_company_txt?.setText(presented_company)

            var presented_name = dataObj?.getString("presented_name")
            property_presented_txt?.setText(presented_name)

            var state = dataObj?.getString("state")
            var zipcode = dataObj?.getString("zipcode")
            var city = dataObj?.getString("city")
            var country = dataObj?.getString("country")
            var posted_date = dataObj?.getString("posted_date")
            var services = dataObj?.getString("services")
            var property_title = dataObj?.getString("property_title")
            var contact_no = dataObj?.getString("contact_no")
            var active_bool = dataObj?.getBoolean("active_bool")

            var property_area = dataObj?.getString("property_area")
            property_area_txt?.setText(property_area)

            var price_type = dataObj?.getString("price_type")
            var post_type = dataObj?.getString("post_type")

        }
    }

    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {
        showErrorBody(error)
    }

    lateinit var mDemoSlider: SliderLayout

    var statesAdapter: StatesAdapter? = null

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
    var acBarListener: Communicator.IActionBar? = null
    var mcontext: Context? = null
    var volleyService: VolleyService? = null
    var token: String? = null
    var itemId = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_details, container, false)
        val b = arguments
        itemId = b!!.getInt("id")
        setIds(view)
        volleyRequests()
        propertyImages()
        return view
    }

    private fun propertyImages() {
        val url_maps = HashMap<String, Int>()
        url_maps["Electronics"] = R.drawable.slider1
        url_maps["Beats Audio"] = R.drawable.slider2
        url_maps["Apple Mackbook Pro"] = R.drawable.slider3
        url_maps["Home Appliances"] = R.drawable.slider4
        url_maps["Sports"] = R.drawable.slider5

        for (name in url_maps.keys) {
            val textSliderView = DefaultSliderView(activity)
            // initialize a SliderLayout
            textSliderView.image(url_maps[name]!!)
                .setScaleType(BaseSliderView.ScaleType.Fit)

            mDemoSlider.run {
                setBackgroundColor(Color.TRANSPARENT)
                setPresetTransformer(SliderLayout.Transformer.Default)
                setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
                setCustomAnimation(DescriptionAnimation())
                setDuration(5000)
                textSliderView.bundle(Bundle())
                textSliderView.bundle
                    .putString("extra", name)
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

        featuresArray = ArrayList()


        mDemoSlider = view.findViewById(R.id.property_img);
        mDemoSlider.getPagerIndicator()
            .setDefaultIndicatorColor(getResources().getColor(R.color.Red), getResources().getColor(R.color.gray));
    }

    fun volleyRequests() {
        volleyService?.getDataVolley(RequestType.StringRequest, Urls.urlProperyDetail + itemId + "/", "")
    }

    override fun onResume() {
        super.onResume()
        acBarListener?.actionBarListener("Profile")
        acBarListener?.isBackButtonEnabled(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mcontext = context
        acBarListener = context as Communicator.IActionBar
    }
}
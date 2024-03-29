package com.brainplow.ogrespace.fragments


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.error.VolleyError

import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.activities.MainActivity
import com.brainplow.ogrespace.adapters.PropertyAdapter
import com.brainplow.ogrespace.adapters.StatesAdapter
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.baseclasses.PropertyBaseFragment
import com.brainplow.ogrespace.enums.LayoutType
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.extesnions.showErrorMessage
import com.brainplow.ogrespace.extesnions.showInfoMessage
import com.brainplow.ogrespace.extesnions.showSuccessMessage
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.VolleyParsing
import com.brainplow.ogrespace.kotlin.VolleyService
import com.brainplow.ogrespace.models.PropertyModel
import com.brainplow.ogrespace.models.StateModel
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.DefaultSliderView
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class HomeFragment : PropertyBaseFragment(), Communicator.IVolleResult, Communicator.IStates,Communicator.IFavourites {


    override fun notifySuccess(requestType: RequestType?, response: JSONArray?, url: String, netWorkResponse: Int?) {
        if (url == Urls.urlStates) {
            setStatesAdapter(volleyParsing!!.getStateData(response, 1))
        }

    }

    override fun notifySuccess(requestType: RequestType?, response: String?, url: String, netWorkResponse: Int?) {
        if (url == Urls.urlGetLeaseProperties) {
            setLeasePropertyAdapter(leaseParsing!!.getPropertyData(JSONObject(response), 1))
        } else if (url == Urls.urlGetSaleProperties)
            setSalePropertyAdapter(volleyParsing!!.getPropertyData(JSONObject(response), 1))
        else if (url == Urls.urlGetRecentlyViewed)
            setRecentPropertyAdapter(recentParsing!!.getPropertyData(JSONObject(response), 1))
        else if(url.contains(Urls.urlDelFav,true)){
            MainActivity.favItemsMap.remove(favId.toString())
            context?.showInfoMessage("Item deleted from favourite successfully")
        }
    }

    override fun notifySuccess(requestType: RequestType?, response: JSONObject?, url: String, netWorkResponse: Int?) {
         if(url.equals(Urls.urlAddToFav)){
             if(netWorkResponse==200) {
                 MainActivity.favItemsMap.put(favId.toString(),favId!!)
                 context?.showSuccessMessage("Item added to favourite successfully")
             }
             else if(netWorkResponse==202) {
                 deleteFromFav(favId)
             }

        }

    }

    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {
        if (url == Urls.urlStates) {
            showErrorBody(error)
        }  else if(!url.equals(Urls.urlAddToFav)){
            //showErrorBody(error)
            context?.showErrorMessage("Item not added to favourite")
        }
        else if(url.equals(Urls.urlDelFav)){
            context?.showErrorMessage("Item not deleted from favourite")
        }
    }

    override fun onStateItemClick(id: Int?, name: String?) {
        navigateToMoreProperties(id, name, 1)

    }

    var volleyService: VolleyService? = null
    var volleyParsing: VolleyParsing? = null
    var leaseParsing: VolleyParsing? = null
    var recentParsing: VolleyParsing? = null
    var mcontext: Context? = null
    var acBarListener: Communicator.IActionBar? = null
    var main_search_edit: EditText? = null
    var saleMoreText:TextView?=null
    var leaseMoreText:TextView?=null
    var recentMoreText:TextView?=null
    var rootView: LinearLayout? = null
    var token: String? = null
    lateinit var mDemoSlider: SliderLayout
    lateinit var bottomNavigation: BottomNavigationView
    var bottomBarListener: Communicator.IBottomBar? = null
    lateinit var recycleStates: RecyclerView
    lateinit var propertiesForSaleRecycler: RecyclerView
    lateinit var propertiesForLeaseRecycler: RecyclerView
    lateinit var propertiesRecentRecycler: RecyclerView
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mcontext = context
        acBarListener = context as Communicator.IActionBar
        bottomBarListener = context as Communicator.IBottomBar
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.setIVolleyResult(this)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
//        acBotListeners()
        setIds(view)
        cursorVisible()

        setListeners()
        volleyRequests()
        return view
    }

    fun acBotListeners() {
        acBarListener?.run {
            actionBarListener("Home")
            toolbarBackground(true)
            optionMenuVisibility(true)
            toolbarColor(true)
            searchlogo(false)
            isBackButtonEnabled(false)
            isSearchVisible(true)
        }

        bottomBarListener?.isBottomVisible(true)

    }

    override fun onResume() {
        super.onResume()
        acBotListeners()
        homeSlider()
        bottomNavigation.menu.findItem(R.id.bottom_home).setChecked(true)
    }

    override fun onStop() {
        super.onStop()
        acBarListener?.isSearchVisible(false)
        bottomBarListener?.isBottomVisible(false)
        acBarListener?.toolbarColor(false)
        acBarListener?.toolbarBackground(false)
    }

    fun cursorVisible() {
        rootView!!.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                val r = Rect()
                rootView!!.getWindowVisibleDisplayFrame(r)
                val screenHeight = rootView!!.getRootView().getHeight()


                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                val keypadHeight = screenHeight - r.bottom;

                //            Log.d("KeyBoard", "keypadHeight = " + keypadHeight)

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    main_search_edit?.isCursorVisible = true


                } else {

                    // if(main_search_edit!=null)
                    main_search_edit?.isCursorVisible = false

                }
            }

        })

    }

    fun setIds(view: View) {
        val sharedPreferences = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
        token = sharedPreferences?.getString("token", "")
        volleyParsing = VolleyParsing()
        leaseParsing = VolleyParsing()
        recentParsing = VolleyParsing()
        volleyService = VolleyService(this, mcontext!!.applicationContext)
        rootView = view.findViewById(R.id.homeFragment)
        main_search_edit = activity?.findViewById(R.id.mainsearch_edittext)
        bottomNavigation = activity!!.findViewById(R.id.navigation)
        saleMoreText=view.findViewById(R.id.p_sale_more)
        leaseMoreText=view.findViewById(R.id.p_lease_more)
        recentMoreText=view.findViewById(R.id.p_recently_more)
        mDemoSlider = view.findViewById(R.id.banner1);
        mDemoSlider.getPagerIndicator()
            .setDefaultIndicatorColor(getResources().getColor(R.color.Red), getResources().getColor(R.color.gray));
        recycleStates = view.findViewById(R.id.recyclerStates)
        propertiesForSaleRecycler = view.findViewById(R.id.p_sale_recycler)
        propertiesForLeaseRecycler = view.findViewById(R.id.p_lease_recycler)
        propertiesRecentRecycler = view.findViewById(R.id.p_recent_recycler)
        propertiesForSaleRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        propertiesForLeaseRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        propertiesRecentRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recycleStates.layoutManager =LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

    }

    fun volleyRequests() {
        volleyService?.getDataVolley(RequestType.ArrayRequest, Urls.urlStates, "")
        volleyService?.getDataVolley(RequestType.StringRequest, Urls.urlGetSaleProperties, "")
        volleyService?.getDataVolley(RequestType.StringRequest, Urls.urlGetLeaseProperties, "")
        volleyService?.getDataVolley(RequestType.StringRequest, Urls.urlGetRecentlyViewed, token!!)

    }

    private fun homeSlider() {
        val url_maps = ArrayList<Int>()
        url_maps.add( R.drawable.slider1)
        url_maps.add( R.drawable.slider2)
        url_maps.add(R.drawable.slider3)
        url_maps.add(R.drawable.slider4)
        url_maps.add(R.drawable.slider5)

        for (i in 0 until url_maps.size) {
            val textSliderView = DefaultSliderView(activity)
            // initialize a SliderLayout
            textSliderView.image(url_maps.get(i))
                .setScaleType(BaseSliderView.ScaleType.Fit)

            mDemoSlider.run {
                setBackgroundColor(Color.TRANSPARENT)
                setPresetTransformer(SliderLayout.Transformer.Default)
                setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
                setCustomAnimation(DescriptionAnimation())
                setDuration(5000)
                textSliderView.bundle(Bundle())
                textSliderView.bundle
                    .putInt("extra", url_maps.get(i))
                addSlider(textSliderView)
            }

            //add your extra information


        }
    }

    private fun setStatesAdapter(stateList: ArrayList<StateModel>) {
        val statesAdapter = StatesAdapter(context, stateList, LayoutType.LayoutStatesCat)
        recycleStates.adapter = statesAdapter
        statesAdapter.run {
            setStateListener(this@HomeFragment)
        }

    }
    private fun setSalePropertyAdapter(propertyList: ArrayList<PropertyModel>) {
        val propertyAdapter = PropertyAdapter(context, propertyList, LayoutType.LayoutHorizontalProperties)
        propertyAdapter.run{
            setFavouriteListener(this@HomeFragment)
            setItemClickListener(this@HomeFragment)
        }
        propertiesForSaleRecycler.adapter = propertyAdapter
    }
    private fun setLeasePropertyAdapter(propertyList: ArrayList<PropertyModel>) {
        val propertyAdapter = PropertyAdapter(context, propertyList, LayoutType.LayoutHorizontalProperties)
        propertyAdapter.run{
            setFavouriteListener(this@HomeFragment)
            setItemClickListener(this@HomeFragment)
        }
        propertiesForLeaseRecycler.adapter = propertyAdapter
    }
    private fun setRecentPropertyAdapter(propertyList: ArrayList<PropertyModel>) {
        val propertyAdapter = PropertyAdapter(context, propertyList, LayoutType.LayoutHorizontalProperties)
        propertyAdapter.run{
            setFavouriteListener(this@HomeFragment)
            setItemClickListener(this@HomeFragment)
        }
        propertiesRecentRecycler.adapter = propertyAdapter
    }
    fun navigateToMoreProperties(id: Int?, name: String?,mflag:Int){
        val args = Bundle()
        args.putInt("mflag", mflag)
        args.putInt("stateId", id!!)
        args.putString("stateName", name)
        val fragment = PropertiesMoreFragment()
        fragment.arguments = args
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.run {
            replace(R.id.content_frame, fragment)
            addToBackStack(fragment.toString())
            commit()

        }
    }
     @SuppressLint("ClickableViewAccessibility")
    fun setListeners(){
         main_search_edit?.setText("")
        saleMoreText?.setOnClickListener(){
            navigateToMoreProperties(0,"",2)
        }
        leaseMoreText?.setOnClickListener(){
            navigateToMoreProperties(0,"",3)
        }
         recentMoreText?.setOnClickListener(){
             navigateToMoreProperties(0,"",4)
         }
        main_search_edit!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {

                if (event.action == android.view.MotionEvent.ACTION_DOWN) {

                  // navigateToFragment(SearchFragment())
                    val f=fragmentManager?.findFragmentByTag("Search")
                    val fragment=SearchFragment()
                    val transaction=fragmentManager?.beginTransaction()
                    if(f==null){
                        transaction?.replace(R.id.content_frame, fragment, "Search")
                        transaction?.run {
                            addToBackStack(null)
                            commit()
                        }
                    }else{

                    }

                }
                return false
            }
        })

    }
}

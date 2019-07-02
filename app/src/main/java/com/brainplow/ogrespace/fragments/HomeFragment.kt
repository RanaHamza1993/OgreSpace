package com.brainplow.ogrespace.fragments


import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.LinearLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.error.VolleyError

import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.adapters.StatesAdapter
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.baseclasses.BaseFragment
import com.brainplow.ogrespace.enums.LayoutType
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.VolleyParsing
import com.brainplow.ogrespace.kotlin.VolleyService
import com.brainplow.ogrespace.models.StateModel
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.DefaultSliderView
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class HomeFragment : BaseFragment(),Communicator.IVolleResult {

    override fun notifySuccess(requestType: String?, response: JSONArray?, url: String) {
        if(url == Urls.urlStates){
            setStatesAdapter(volleyParsing!!.getStateData(response,1))
        }

    }

    override fun notifySuccess(requestType: String?, response: String?, url: String) {

    }

    override fun notifySuccess(requestType: String?, response: JSONObject?, url: String, netWorkResponse: Int?) {

    }

    override fun notifyError(requestType: String?, error: VolleyError?, url: String) {
        if(url == Urls.urlStates){
            showErrorBody(error)
        }
    }
    var volleyService: VolleyService? = null
    var volleyParsing: VolleyParsing? = null
    var main_search_edit: EditText? = null
    var rootView: LinearLayout? = null
    lateinit var mDemoSlider: SliderLayout
    var mcontext:Context?=null
    var acBarListener:Communicator.IActionBar?=null
    var bottomBarListener:Communicator.IBottomBar?=null

    lateinit var recycleStates: RecyclerView
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mcontext=context
        acBarListener=context as Communicator.IActionBar
        bottomBarListener=context as Communicator.IBottomBar
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        acBotListeners()
        setIds(view)
        cursorVisible()
        homeSlider()
        volleyRequests()
        return view
    }

    fun acBotListeners(){
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
    }

    override fun onStop() {
        super.onStop()
        acBarListener?.isSearchVisible(false)
        bottomBarListener?.isBottomVisible(false)
        acBarListener?.toolbarColor(false)
        acBarListener?.toolbarBackground(false)
    }
    fun cursorVisible(){
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
    fun setIds(view:View){
        volleyParsing= VolleyParsing()
        volleyService = VolleyService(this, mcontext!!.applicationContext)
        rootView = view.findViewById(R.id.homeFragment)
        main_search_edit = activity?.findViewById(R.id.mainsearch_edittext)
        mDemoSlider = view.findViewById(R.id.banner1);
        mDemoSlider.getPagerIndicator().setDefaultIndicatorColor(getResources().getColor(R.color.Red), getResources().getColor(R.color.gray));
        recycleStates = view.findViewById(R.id.recyclerStates)
        recycleStates.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    fun volleyRequests(){
        volleyService?.getDataVolley("Array", Urls.urlStates, "")

    }
    private fun homeSlider() {


        val url_maps = HashMap<String, Int>()
        url_maps["Electronics"] = R.drawable.slider1
        url_maps["Beats Audio"] = R.drawable.slider2
        url_maps["Apple Mackbook Pro"] =R.drawable.slider3
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
    fun setStatesAdapter(stateList:ArrayList<StateModel>){
        val statesAdapter = StatesAdapter(context,stateList,LayoutType.LayoutStatesCat)
        recycleStates.adapter = statesAdapter
       // statesAdapter.notifyDataSetChanged()
    }
}

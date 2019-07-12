package com.brainplow.ogrespace.fragments


import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.error.VolleyError
import com.badoo.mobile.util.WeakHandler

import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.adapters.SearchKeyWordsAdapter
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.baseclasses.BaseFragment
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.MySingleton
import com.brainplow.ogrespace.kotlin.VolleyService
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.launch
import org.json.JSONObject

class SearchFragment : BaseFragment(), Communicator.IVolleResult {


    override fun notifySuccess(requestType: RequestType?, response: JSONObject?, url: String, netWorkResponse: Int?) {
        spinner_layout?.visibility = View.GONE


      //  searchIcon?.visibility = View.GONE
        keyWordsList.clear()
        val predictions = response?.getJSONArray("predictions")
        for (i in 0 until predictions!!.length()) {
            keyWordsList.add(predictions.getJSONObject(i).getString("description"))
        }
        if (drawerLayout!!.isDrawerOpen(GravityCompat.END)) {
            d_suggestion_recycler?.visibility=View.VISIBLE
            setDrawerSuggestionAdapter()
        }
        else
        setAdapter()
    }

    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {
        showErrorBody(error)
    }

    var navigationView: NavigationView? = null
    var drawerLayout: DrawerLayout? = null
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    var toolbar: Toolbar? = null
    var typeSpinner: Spinner? = null
    var dTypeSpinner: Spinner? = null
    var dSpaceTypeSpinner: Spinner? = null
    var minPriceSpinner: Spinner? = null
    var maxPriceSpinner: Spinner? = null
    var minSpaceSpinner: Spinner? = null
    var maxSpaceSpinner: Spinner? = null
    var typeList = arrayOf("Please select property type", "Sale", "Lease")
    var spaceTypeList = arrayOf("Please select space type", "Offices", "Retail","Co Working","Land","Industrial","Medical Offices")
    var minSpaceList = arrayOf("Please select min space", "500 sqft", "1000 sqft","1500 sqft","2000 sqft","2500 sqft","3000 sqft")
    var maxSpaceList = arrayOf("Please select max space", "500 sqft", "1000 sqft","1500 sqft","2000 sqft","2500 sqft","3000 sqft")
    var minPriceList = arrayOf("Please select min price", "$"+ "Any", "$"+"100,000","$"+"200,000","$"+"300,000","$"+ "400,0000","$"+ "500,000")
    var maxPriceList = arrayOf("Please select max price", "$"+ "Any", "$"+"100,000","$"+"200,000","$"+"300,000","$"+ "400,0000","$"+ "500,000")
    var keyWordsList = ArrayList<String>()
    var searchAdapter: SearchKeyWordsAdapter? = null
    var main_search_edit: EditText? = null
    lateinit var main_edit_cross: ImageView
    lateinit var main_edit_mic: ImageView
    var searchIcon: ImageView? = null
    var searchQuery = ""
    var isRunning = false
    var acBarListener: Communicator.IActionBar? = null
    var mcontext: Context? = null
    var volleyService: VolleyService? = null
    var suggestionRecycler: RecyclerView? = null
    var d_suggestion_recycler: RecyclerView? = null
    var rootView: RelativeLayout? = null
    var d_search_edit_text: TextView? = null
    var dSearch: TextView? = null
    var dClear: TextView? = null
    var d_search_edit_textwatcher: TextWatcher? = null
    var main_search_textWatcher: TextWatcher? = null
    var type = ""
    val drawerSuggestionListener=object :SearchKeyWordsAdapter.ISearchListener{
        override fun onItemClick(position: Int, keyWord: String) {
            d_search_edit_text?.removeTextChangedListener(d_search_edit_textwatcher)
            d_search_edit_text?.setText(keyWord)
            d_search_edit_text?.addTextChangedListener(d_search_edit_textwatcher)
            d_suggestion_recycler?.visibility=View.GONE
        }

    }
    val searchListener = object : SearchKeyWordsAdapter.ISearchListener {
        override fun onItemClick(position: Int, keyWord: String) {
            val args = Bundle()
            args.putString("keyword", keyWord)
            args.putString("type", type)
            val fragment = SearchResult()
            fragment.arguments = args
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.run {
                replace(R.id.content_frame, fragment)
                addToBackStack(null)
                commit()
            }

        }

    }

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
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        setIds(view)
        rootViewObserver()
        setListeners()
        return view
    }

    override fun onResume() {
        super.onResume()
        isRunning = true
        acBarListener?.actionBarListener("Search")
        acBarListener?.run{
            isSearchVisible(true)
            toolbarColor(true)
            backArrow(false)
            isBackButtonEnabled(true)
        }

    }

    override fun onStop() {
        super.onStop()
        isRunning = false
        acBarListener?.isSearchVisible(false)
        acBarListener?.toolbarColor(false)
        acBarListener?.toolbarBackground(false)

    }

    private fun setIds(view: View) {
        navigationView = view.findViewById(R.id.search_nav_view)
        val navHeader = navigationView?.getHeaderView(0)
        d_search_edit_text=navHeader?.findViewById(R.id.fs_search_edit)
        d_suggestion_recycler=navHeader?.findViewById(R.id.fs_suggestions_recycler)
        dTypeSpinner=navHeader?.findViewById(R.id.s_property_type)
        dSearch=navHeader?.findViewById(R.id.search_filter)
        dClear=navHeader?.findViewById(R.id.clear_filer)
        dSpaceTypeSpinner=navHeader?.findViewById(R.id.s_space_type)
        minPriceSpinner=navHeader?.findViewById(R.id.s_min_price)
        maxPriceSpinner=navHeader?.findViewById(R.id.s_max_price)
        minSpaceSpinner=navHeader?.findViewById(R.id.s_min_space)
        maxSpaceSpinner=navHeader?.findViewById(R.id.s_max_space)
        drawerLayout = view.findViewById(R.id.search_drawer)
        rootView=view.findViewById(R.id.searchSuggestionRoot)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
        searchIcon = view.findViewById(R.id.search_icon)
        volleyService = VolleyService(this, mcontext!!.applicationContext)
        suggestionRecycler = view.findViewById(R.id.suggestions_recycler)
        suggestionRecycler?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        d_suggestion_recycler?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        main_search_edit = activity?.findViewById(R.id.mainsearch_edittext)
        main_edit_cross = activity!!.findViewById(R.id.edit_cross)
        main_edit_mic = activity!!.findViewById(R.id.edit_mic)
        typeSpinner=view.findViewById(R.id.spinner_type)
    }

    fun rootViewObserver(){
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
    fun setListeners() {

       // if (!searchQuery.equals(""))
          //  searchIcon?.visibility = View.GONE

        main_search_textWatcher=object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                MySingleton.getInstance(mcontext!!).requestQueue.cancelAll("searchRequest")
                var value = main_search_edit?.text.toString()
                value = value.replace(" ", "%20")
                if (isRunning&&!value.equals(""))
                    getSearchResult(value)
                else {
                    keyWordsList.clear()
                    setAdapter()
                }
                main_edit_cross.visibility = View.VISIBLE
                main_edit_mic.visibility = View.GONE

                main_edit_cross.setOnClickListener {
                    main_search_edit?.setText("")


                }
            }

            override fun afterTextChanged(s: Editable?) {

                if (TextUtils.isEmpty(s.toString().trim())) {

                    main_edit_cross.visibility = View.GONE
                    main_edit_mic.visibility = View.VISIBLE
                    //   searchIcon?.visibility = View.VISIBLE
                    spinner_layout?.visibility = View.VISIBLE


                }
            }

        }
        main_search_edit!!.addTextChangedListener(main_search_textWatcher)

        d_search_edit_textwatcher=object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                MySingleton.getInstance(mcontext!!).requestQueue.cancelAll("searchRequest")
                var value = d_search_edit_text?.text.toString()
                value = value.replace(" ", "%20")
                if (isRunning&&!value.equals(""))
                    getSearchResult(value)
                else {
                    keyWordsList.clear()
                    setDrawerSuggestionAdapter()
                }
//                main_edit_cross.visibility = View.VISIBLE
//                main_edit_mic.visibility = View.GONE
//
//                main_edit_cross.setOnClickListener {
//                    main_search_edit?.setText("")
//
//
//                }
            }

            override fun afterTextChanged(s: Editable?) {

                if (TextUtils.isEmpty(s.toString().trim())) {

//                    main_edit_cross.visibility = View.GONE
//                    main_edit_mic.visibility = View.VISIBLE
//                    //   searchIcon?.visibility = View.VISIBLE
                    d_suggestion_recycler?.visibility=View.GONE


                }
            }

        }
        d_search_edit_text?.addTextChangedListener(d_search_edit_textwatcher)


        val typeAdapter = ArrayAdapter<String>(context, R.layout.spinner_item_layout, typeList)
        val spaceTypeAdapter = ArrayAdapter<String>(context, R.layout.spinner_item_layout, spaceTypeList)
        val minPriceAdapter = ArrayAdapter<String>(context, R.layout.spinner_item_layout, minPriceList)
        val maxPriceAdapter = ArrayAdapter<String>(context, R.layout.spinner_item_layout, maxPriceList)
        val minSpaceAdapter = ArrayAdapter<String>(context, R.layout.spinner_item_layout, minSpaceList)
        val maxSpaceAdapter = ArrayAdapter<String>(context, R.layout.spinner_item_layout, maxSpaceList)
        typeAdapter.setDropDownViewResource(R.layout.spinner_item_layout)
        minPriceAdapter.setDropDownViewResource(R.layout.spinner_item_layout)
        maxPriceAdapter.setDropDownViewResource(R.layout.spinner_item_layout)
        spaceTypeAdapter.setDropDownViewResource(R.layout.spinner_item_layout)
        minSpaceAdapter.setDropDownViewResource(R.layout.spinner_item_layout)
        maxSpaceAdapter.setDropDownViewResource(R.layout.spinner_item_layout)
        typeSpinner?.adapter=typeAdapter
        dTypeSpinner?.adapter=typeAdapter
        dSpaceTypeSpinner?.adapter=spaceTypeAdapter
        minPriceSpinner?.adapter=minPriceAdapter
        maxPriceSpinner?.adapter=maxPriceAdapter
        minSpaceSpinner?.adapter=minSpaceAdapter
        maxSpaceSpinner?.adapter=maxSpaceAdapter
        minPriceSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


            }
        }
        maxPriceSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


            }
        }

        typeSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (position == 0)
                    type = ""
                else if (position == 1)
                    type = "Sale"
                else if (position == 2)
                    type = "lease"

            }
        }
        dTypeSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (position == 0)
                    type = ""
                else if (position == 1)
                    type = "Sale"
                else if (position == 2)
                    type = "lease"

            }
        }
        dSpaceTypeSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }
        }

        dClear?.setOnClickListener {
            clearFilters()
        }
    }

    private fun getSearchResult(value: String) {
        volleyService?.getDataVolley(
            RequestType.JsonObjectRequest,
            Urls.urlGooglePlaceSearch + value + "&key=AIzaSyBvtXUC9gCiJTPRwX-tCHsOgTiLo2H8P6Q",
            "","searchRequest"
        )

    }

    fun setAdapter() {
        if (!keyWordsList.isEmpty()) {
            val list: List<String> = keyWordsList.distinct()
            searchAdapter = SearchKeyWordsAdapter(list, searchListener)
        } else
            searchAdapter = SearchKeyWordsAdapter(keyWordsList, searchListener)
        suggestionRecycler?.adapter = searchAdapter
        searchAdapter?.notifyDataSetChanged()
    }

    fun setDrawerSuggestionAdapter() {
        if (!keyWordsList.isEmpty()) {
            val list: List<String> = keyWordsList.distinct()
            searchAdapter = SearchKeyWordsAdapter(list, drawerSuggestionListener)
        } else
            searchAdapter = SearchKeyWordsAdapter(keyWordsList, drawerSuggestionListener)
        d_suggestion_recycler?.adapter = searchAdapter
        searchAdapter?.notifyDataSetChanged()
    }

    fun onBackPressed(){
        if (drawerLayout!!.isDrawerOpen(GravityCompat.END)) {
            drawerLayout?.closeDrawer(GravityCompat.END)


        }else{
            fragmentManager?.popBackStack()
        }
    }
    fun clearFilters(){
        d_search_edit_text?.setText("")
        maxSpaceSpinner?.setSelection(0)
        minSpaceSpinner?.setSelection(0)
        maxPriceSpinner?.setSelection(0)
        minPriceSpinner?.setSelection(0)
        dTypeSpinner?.setSelection(0)
        dSpaceTypeSpinner?.setSelection(0)
    }
}

package com.brainplow.ogrespace.fragments


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
        setAdapter()
    }

    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {
        showErrorBody(error)
    }

    var typeSpinner: Spinner? = null
    var typeList = arrayOf("Please select property type", "Sale", "Lease")
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
    var type = ""
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
        setListeners()
        return view
    }

    override fun onResume() {
        super.onResume()
        isRunning = true
        //acBarListener?.actionBarListener("Home")
        acBarListener?.isSearchVisible(true)
        acBarListener?.toolbarBackground(true)
        acBarListener?.isBackButtonEnabled(false)
    }

    override fun onStop() {
        super.onStop()
        isRunning = false
        acBarListener?.isSearchVisible(false)
        acBarListener?.toolbarColor(false)
        acBarListener?.toolbarBackground(false)

    }

    private fun setIds(view: View?) {
        searchIcon = view?.findViewById(R.id.search_icon)
        volleyService = VolleyService(this, mcontext!!.applicationContext)
        suggestionRecycler = view?.findViewById(R.id.suggestions_recycler)
        suggestionRecycler?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        main_search_edit = activity?.findViewById(R.id.mainsearch_edittext)
        main_edit_cross = activity!!.findViewById(R.id.edit_cross)
        main_edit_mic = activity!!.findViewById(R.id.edit_mic)
        typeSpinner=view?.findViewById(R.id.spinner_type)
    }

    fun setListeners() {

       // if (!searchQuery.equals(""))
          //  searchIcon?.visibility = View.GONE

        main_search_edit!!.addTextChangedListener(object : TextWatcher {
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

        })


        val typeAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, typeList)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner?.adapter=typeAdapter
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
    }

    private fun getSearchResult(value: String) {
        volleyService?.getDataVolley(
            RequestType.JsonObjectRequest,
            Urls.urlGooglePlaceSearch + value + "&key=AIzaSyBvtXUC9gCiJTPRwX-tCHsOgTiLo2H8P6Q",
            ""
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


}
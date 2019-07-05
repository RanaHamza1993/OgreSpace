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
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.error.VolleyError

import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.adapters.SearchKeyWordsAdapter
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.baseclasses.BaseFragment
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.MySingleton
import com.brainplow.ogrespace.kotlin.VolleyService
import org.json.JSONObject

class SearchFragment : BaseFragment(),Communicator.IVolleResult {


    override fun notifySuccess(requestType: RequestType?, response: JSONObject?, url: String, netWorkResponse: Int?) {
        val a=response
        val b=5
    }
    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {
        showErrorBody(error)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mcontext = context
    }
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
    val searchListener = object : SearchKeyWordsAdapter.ISearchListener {
        override fun onItemClick(position: Int, keyWord: String) {
            //  val f = fragmentManager?.findFragmentByTag("Search")
            //navigateToSearch(position,keyWord)

        }

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
        isRunning=true
    }

    override fun onStop() {
        super.onStop()
        isRunning=false
    }
    private fun setIds(view: View?) {
        volleyService = VolleyService(this, mcontext!!.applicationContext)
        suggestionRecycler = view?.findViewById(R.id.suggestions_recycler)
        suggestionRecycler?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        main_search_edit = activity?.findViewById(R.id.mainsearch_edittext)
        main_edit_cross = activity!!.findViewById(R.id.edit_cross)
        main_edit_mic = activity!!.findViewById(R.id.edit_mic)
    }

    fun setListeners() {

        main_search_edit!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                MySingleton.getInstance(mcontext!!).requestQueue.cancelAll("searchRequest")
                var value = main_search_edit?.text.toString()
                value = value.replace(" ", "%20")
                if (isRunning)
                    getSearchResult(value)
                main_edit_cross.visibility = View.VISIBLE
                main_edit_mic.visibility = View.GONE

                main_edit_cross.setOnClickListener {
                    main_search_edit?.setText("")
//                    handler?.postDelayed({
//                        searchIcon?.visibility = View.VISIBLE
//                    }, 160)

                }
            }

            override fun afterTextChanged(s: Editable?) {

                if (TextUtils.isEmpty(s.toString().trim())) {

                    main_edit_cross.visibility = View.GONE
                    main_edit_mic.visibility = View.VISIBLE


                }
            }

        })

    }

    private fun getSearchResult(value: String) {
        volleyService?.getDataVolley(RequestType.JsonObjectRequest, Urls.urlGooglePlaceSearch+value+"&key=AIzaSyCwFuBLNlN0azRUJ80g93QJ_RJLGRXVCCc","")

    }

    fun setAdapter() {
        if(!keyWordsList.isEmpty()) {
            val list: List<String> = keyWordsList.distinct()
            searchAdapter = SearchKeyWordsAdapter(list, searchListener)
        }else
            searchAdapter = SearchKeyWordsAdapter(keyWordsList, searchListener)
        suggestionRecycler?.adapter = searchAdapter
        searchAdapter?.notifyDataSetChanged()
    }

}

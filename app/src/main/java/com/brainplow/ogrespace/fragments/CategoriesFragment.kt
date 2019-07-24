package com.brainplow.ogrespace.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.adapters.CategoriesAdapter
import com.brainplow.ogrespace.adapters.StatesAdapter
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.baseclasses.PropertyBaseFragment
import com.brainplow.ogrespace.enums.LayoutType
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.LoadingDialog
import com.brainplow.ogrespace.kotlin.VolleyParsing
import com.brainplow.ogrespace.kotlin.VolleyService
import com.brainplow.ogrespace.models.CategoriesModel
import org.json.JSONArray

class CategoriesFragment : PropertyBaseFragment(),Communicator.IVolleResult {


    var volleyService: VolleyService? = null
    var volleyParsing: VolleyParsing? = null
    lateinit var load: LoadingDialog
    lateinit var recyclerCategories: RecyclerView
    var acBarListener: Communicator.IActionBar? = null
    var mcontext: Context? = null


    override fun notifySuccess(requestType: RequestType?, response: JSONArray?, url: String, netWorkResponse: Int?) {
        if (url == Urls.urlGetPropertyCategories) {
            setCategoryAdapter(volleyParsing!!.getCategoriesData(response))
        }

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mcontext = context
        acBarListener = context as Communicator.IActionBar

    }

    override fun onResume() {
        super.onResume()
        acBarListener?.run {
            isBackButtonEnabled(true)
            toolbarBackground(false)
            actionBarListener("Categories")
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_categories, container, false)
        setIds(view)
        setListeners()
        volleyRequests()
        return view
    }

    private fun setIds(view: View) {
        volleyParsing = VolleyParsing()
        volleyService = VolleyService(this, mcontext!!.applicationContext)
        recyclerCategories = view.findViewById(R.id.recycle_categories)
        recyclerCategories.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private fun setListeners() {

    }

    private fun volleyRequests() {
        volleyService?.getDataVolley(RequestType.ArrayRequest, Urls.urlGetPropertyCategories, "")
    }

    private fun setCategoryAdapter(categories: ArrayList<CategoriesModel>) {
        val statesAdapter = CategoriesAdapter(context, categories, LayoutType.LayoutCategories)
        recyclerCategories.adapter = statesAdapter

    }


}

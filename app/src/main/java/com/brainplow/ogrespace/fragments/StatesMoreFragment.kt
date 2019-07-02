package com.brainplow.ogrespace.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.error.VolleyError

import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.adapters.PropertyAdapter
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.baseclasses.BaseFragment
import com.brainplow.ogrespace.enums.LayoutType
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.LoadingDialog
import com.brainplow.ogrespace.kotlin.VolleyParsing
import com.brainplow.ogrespace.kotlin.VolleyService
import com.brainplow.ogrespace.models.PropertyModel
import org.json.JSONObject
import java.lang.Exception


class StatesMoreFragment : BaseFragment(),Communicator.IVolleResult {

    override fun notifySuccess(requestType: String?, response: JSONObject?, url: String, netWorkResponse: Int?) {
        if (url.contains(Urls.urlPropertyByState)) {

            if (pages == 1) {
                try {
                    bidItems = response!!.getInt("totalItems")
                    bidPages = response.getInt("totalPages")
                }catch (e:Exception){}
            }
            flag = 1
      //      val list=volleyParsing?.getPropertyData(response,pages)
            setPropertyAdapter(volleyParsing?.getPropertyData(response,pages)!!)
        }
    }

    override fun notifySuccess(requestType: String?, response: String?, url: String) {

    }

    override fun notifyError(requestType: String?, error: VolleyError?, url: String) {
        if (url.contains(Urls.urlPropertyByState)) {
            showErrorBody(error)
        }
    }
    var bidPages = 0
    var bidItems = 0
    var pages = 1
    var acBarListener: Communicator.IActionBar? = null
    var mcontext:Context?=null
    var volleyService: VolleyService? = null
    var volleyParsing: VolleyParsing? = null
    var layoutmanger: LinearLayoutManager? = null
    var propertyAdapter:PropertyAdapter?=null
    var flag = 0
    lateinit var load: LoadingDialog
    lateinit var recycleProperty: RecyclerView
    var stateName:String?=null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mcontext=context
        acBarListener=context as Communicator.IActionBar

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_states_more, container, false)
        val bundle=arguments
        stateName=bundle?.getString("stateName")
        acBarListener?.actionBarListener("Properties")
        acBarListener?.isBackButtonEnabled(true)
        acBarListener?.toolbarColor(false)

        setIds(view)
        setListeners()
        volleyRequests()
        return view
    }

    override fun onResume() {
        super.onResume()
        acBarListener?.run{
            isBackButtonEnabled(true)
            toolbarBackground(false)
        }
    }
    fun setIds(view:View){
        load = LoadingDialog("Loading", context)
        volleyParsing= VolleyParsing()
        volleyService = VolleyService(this, mcontext!!.applicationContext)
        recycleProperty=view.findViewById(R.id.recycle_states_more)
        layoutmanger=LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recycleProperty.layoutManager = layoutmanger
    }

    fun volleyRequests(){
        volleyService?.getDataVolley("Object", Urls.urlPropertyByState+stateName+"/"+ "?page=" + pages, "")

    }
    fun setListeners(){

    }

    fun setPropertyAdapter(propertyList:ArrayList<PropertyModel>){

        if (pages == 1) {
            propertyAdapter = PropertyAdapter(context,propertyList,LayoutType.LayoutProperties)
            recycleProperty.adapter = propertyAdapter
        }

        recycleProperty.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutmanger!!.childCount
                val totalItemCount = propertyAdapter?.itemCount
                val pastVisibleItems = layoutmanger!!.findFirstVisibleItemPosition()
                if (pastVisibleItems + visibleItemCount >= totalItemCount!!) {
                    if ((bidPages != pages) && (flag == 1)) {
                        pages++
                        if (!load.ishowingg())
                            load.showdialog()
                        volleyService!!.getDataVolley("Object", Urls.urlPropertyByState+stateName + "?page=" + pages, "")
                        flag = 0
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        })

            propertyAdapter?.notifyDataSetChanged()
        if (load.ishowingg())
            load.dismisss()
    }
}

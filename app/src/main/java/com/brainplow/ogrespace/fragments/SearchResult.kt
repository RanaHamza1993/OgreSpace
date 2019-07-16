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
import com.brainplow.ogrespace.activities.MainActivity
import com.brainplow.ogrespace.adapters.PropertyAdapter
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.baseclasses.BaseFragment
import com.brainplow.ogrespace.baseclasses.PropertyBaseFragment
import com.brainplow.ogrespace.enums.LayoutType
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.extesnions.showInfoMessage
import com.brainplow.ogrespace.extesnions.showSuccessMessage
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.LoadingDialog
import com.brainplow.ogrespace.kotlin.VolleyParsing
import com.brainplow.ogrespace.kotlin.VolleyService
import com.brainplow.ogrespace.models.FilterSearchModel
import com.brainplow.ogrespace.models.PropertyModel
import kotlinx.android.synthetic.main.fragment_search_result.*
import org.json.JSONObject
import java.util.logging.Filter


class SearchResult : PropertyBaseFragment(), Communicator.IVolleResult {

    override fun notifySuccess(requestType: RequestType?, response: JSONObject?, url: String, netWorkResponse: Int?) {


//        empty_message?.visibility=View.GONE
//        search_icon?.visibility=View.GONE

        if(url.equals(Urls.urlAddToFav)){
            if(netWorkResponse==200) {
                MainActivity.favItemsMap.put(favId.toString(),favId!!)
                context?.showSuccessMessage("Item added to favourite successfully")
            }
            else if(netWorkResponse==202)
                deleteFromFav(favId)

        }else {
            flag = 1
            setSearchAdapter(volleyParsing?.getPropertyData(response, pages)!!)
        }
    }

    override fun notifySuccess(requestType: RequestType?, response: String?, url: String, netWorkResponse: Int?) {
         if(url.contains(Urls.urlDelFav,true)){
            MainActivity.favItemsMap.remove(favId.toString())
            context?.showInfoMessage("Item deleted from favourite successfully")
        }
    }
    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {
        showErrorBody(error)
    }

    private fun setSearchAdapter(propertyList: ArrayList<PropertyModel>) {

        if(propertyList.isEmpty()) {
            empty_message?.visibility = View.VISIBLE
            searchRecycler?.visibility=View.GONE
        }else{
            empty_message?.visibility = View.GONE
            searchRecycler?.visibility=View.VISIBLE
        }
        if (pages == 1) {
            propertyAdapter = PropertyAdapter(context, propertyList, LayoutType.LayoutProperties)
            searchRecycler?.adapter = propertyAdapter
        }

        searchRecycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        getSearchResult()
                        flag = 0
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        })
        propertyAdapter?.run{
            setFavouriteListener(this@SearchResult)
            setItemClickListener(this@SearchResult)
            notifyDataSetChanged()

        }
        if (load.ishowingg())
            load.dismisss()
    }



    var layoutmanger: LinearLayoutManager? = null
    var bidPages = 0
    var bidItems = 0
    var pages = 1
    var flag = 0
    var mflag = 1
    var propertyAdapter: PropertyAdapter? = null
    var searchRecycler: RecyclerView? = null
    var volleyService: VolleyService? = null
    var volleyParsing: VolleyParsing? = null
    var acBarListener: Communicator.IActionBar? = null
    var mcontext: Context? = null
    var keyWord: String? = null
    var filterModel: FilterSearchModel? = null
    var type: String? = null
    var obj: JSONObject? = null
    lateinit var load: LoadingDialog

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
        super.setIVolleyResult(this)
        val view = inflater.inflate(R.layout.fragment_search_result, container, false)
        val bundle = arguments
        filterModel = bundle?.getSerializable("filterModel") as FilterSearchModel
        mflag=bundle.getInt("mflag")
        if(mflag==1) {
            type =    filterModel?.property_type
        }
        keyWord = filterModel?.keyword?.replace("[ -._&@#\$()*!~:,]".toRegex(), "%20")
        obj = JSONObject()
        obj?.put("keyword", keyWord)
        if (!type.equals(""))
            obj?.put("type", type)
        setIds(view)

        getSearchResult()
        return view
    }

    override fun onResume() {
        super.onResume()
        acBarListener?.isSearchVisible(false)
        acBarListener?.toolbarBackground(false)
        acBarListener?.actionBarListener("Search Results")
        acBarListener?.isBackButtonEnabled(true)
    }

    fun setIds(view: View) {
        var bidPages = 0
        var bidItems = 0
        var pages = 1
        load = LoadingDialog("Loading", context)
        volleyParsing = VolleyParsing()
        volleyService = VolleyService(this, mcontext!!.applicationContext)
        searchRecycler = view.findViewById(R.id.search_recycler)
        layoutmanger = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        searchRecycler?.layoutManager = layoutmanger
    }

    private fun getSearchResult() {
        if(mflag==1) {
            volleyService?.postDataVolley(
                RequestType.JsonObjectRequest,
                Urls.urlSearchProperties + "?page=" + pages,
                obj!!,
                ""
            )
        }else if(mflag==2){
            val ob=JSONObject()
            ob.put("keyword",keyWord)
            if(!filterModel?.post_type.equals(""))
            ob.put("post_type",filterModel?.post_type)
            if(!filterModel?.property_type.equals(""))
            ob.put("property_type",filterModel?.property_type)
            if(!filterModel?.pricelowlimit.equals(""))
            ob.put("pricelowlimit",filterModel?.pricelowlimit)
            if(!filterModel?.pricehighlimit.equals(""))
            ob.put("pricehighlimit",filterModel?.pricehighlimit)
            if(!filterModel?.spacelowlimit.equals(""))
            ob.put("spacelowlimit",filterModel?.spacelowlimit)
            if(!filterModel?.spacehighlimit.equals(""))
            ob.put("spacehighlimit",filterModel?.spacehighlimit)
            volleyService?.postDataVolley(
                RequestType.JsonObjectRequest,
                Urls.urlFilterSearch + "?page=" + pages,
                ob,
                ""
            )
        }


    }
}

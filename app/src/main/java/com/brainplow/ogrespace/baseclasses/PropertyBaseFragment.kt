package com.brainplow.ogrespace.baseclasses


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.fragments.PropertyDetailFragment
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.JWTUtils
import com.brainplow.ogrespace.kotlin.VolleyService
import org.json.JSONObject

open class PropertyBaseFragment : BaseFragment(), Communicator.IFavourites,Communicator.IItemDetail {

    override fun onItemClick(id: Int?) {
        val args=Bundle()
        args.putInt("id",id!!)
        val fragment = PropertyDetailFragment()
        fragment.arguments = args
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.run {
            replace(R.id.content_frame, fragment)
            addToBackStack(fragment.toString())
            commit()
        }
    }

    override fun addToFav(id: Int?) {
        favId = id
        val obj = JSONObject().put("property_id", id)
        volleyService?.postDataVolley(RequestType.JsonObjectRequest, Urls.urlAddToFav, obj, token!!)
    }

    override fun deleteFromFav(id: Int?) {
        favId = id
        volleyService?.deleteDataVolley(RequestType.StringRequest, Urls.urlDelFav + id, token!!)
    }

    private var token: String? = null
    private var volleyService: VolleyService? = null
    private var volleyResult: Communicator.IVolleResult? = null
    private var mcontext: Context? = null
    private var sharedPreferences: SharedPreferences? = null
    var favId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mcontext = context;

    }

    fun setIVolleyResult(volleyResult: Communicator.IVolleResult?) {
        sharedPreferences = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
        token = sharedPreferences?.getString("token", "")
        volleyService = VolleyService(volleyResult, mcontext!!.applicationContext)
    }


}

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
import com.brainplow.ogrespace.adapters.StatesAdapter
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.baseclasses.PropertyBaseFragment
import com.brainplow.ogrespace.enums.LayoutType
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.LoadingDialog
import com.brainplow.ogrespace.kotlin.VolleyParsing
import com.brainplow.ogrespace.kotlin.VolleyService
import com.brainplow.ogrespace.models.StateModel
import org.json.JSONArray

class StatesFragment : PropertyBaseFragment(), Communicator.IVolleResult,Communicator.IStates {


    override fun onStateItemClick(id: Int?, name: String?) {
        navigateToMoreProperties(id, name, 1)

    }
    override fun notifySuccess(requestType: RequestType?, response: JSONArray?, url: String, netWorkResponse: Int?) {
        if (url == Urls.urlStates) {
            setStatesAdapter(volleyParsing!!.getStateData(response, 1))
        }

    }

    var volleyService: VolleyService? = null
    var volleyParsing: VolleyParsing? = null
    lateinit var load: LoadingDialog
    lateinit var recycleStates: RecyclerView
    var acBarListener: Communicator.IActionBar? = null
    var mcontext: Context? = null

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
            actionBarListener("States")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.setIVolleyResult(this)
        val view = inflater.inflate(R.layout.fragment_states, container, false)
        setIds(view)
        setListeners()
        volleyRequests()
        return view

    }

    private fun setIds(view: View) {
        volleyParsing = VolleyParsing()
        volleyService = VolleyService(this, mcontext!!.applicationContext)
        recycleStates = view.findViewById(R.id.recycle_states)
        recycleStates.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private fun setListeners() {

    }

    private fun volleyRequests() {
        volleyService?.getDataVolley(RequestType.ArrayRequest, Urls.urlStates, "")
    }

    private fun setStatesAdapter(stateList: ArrayList<StateModel>) {
        val statesAdapter = StatesAdapter(context, stateList, LayoutType.LayoutStatesVertical)
        recycleStates.adapter = statesAdapter
        statesAdapter.run {
            setStateListener(this@StatesFragment)
        }
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
}

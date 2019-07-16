package com.brainplow.ogrespace.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.interfaces.Communicator

class NotificationsFragment : Fragment() {

    var acBarListener: Communicator.IActionBar? = null
    var mcontext: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mcontext = context
        acBarListener = context as Communicator.IActionBar
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_notifications, container, false)
        setIds(view)
        setListeners()
        return view
    }

    override fun onResume() {
        super.onResume()
        acBarListener?.actionBarListener("Notifications")
        acBarListener?.isBackButtonEnabled(true)
    }

    fun setIds(view:View){

    }
    fun setListeners(){

    }



}

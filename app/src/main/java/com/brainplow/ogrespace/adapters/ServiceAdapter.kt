package com.brainplow.ogrespace.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.fragments.AddListingFragment
import com.brainplow.ogrespace.models.StateModel
import com.bumptech.glide.Glide
import org.json.JSONArray

class ServiceAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mContext: Context? = null
    var sServiceArray: ArrayList<StateModel>? = null


    constructor(mContext: Context, sServiceArray: ArrayList<StateModel>) : this() {
        this.mContext = mContext
        this.sServiceArray = sServiceArray

    }

    constructor(mContext: Context) : this() {
        this.mContext = mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.service_item, parent, false))
        } else {
            return ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.checked_service_item, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return sServiceArray?.size!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var holder: ViewHolder = holder as ViewHolder
        var user: String? = null
        var sb_user = StringBuilder()
        var myList = JSONArray()
        var user_pref: String? = null


        holder.service_title?.setText(sServiceArray?.get(position)!!.state)

        Glide.with(mContext!!)
            .load(sServiceArray?.get(position)?.icon_image)
            .into(holder.service_image!!);

        holder?.main_layout?.setOnClickListener(View.OnClickListener {
            if (sServiceArray?.get(position)!!.check_value == false) {
                sServiceArray?.get(position)!!.check_value = true
            } else {
                sServiceArray?.get(position)!!.check_value = false
            }

            for (i in 0 until sServiceArray!!.size)
                if (sServiceArray?.get(i)?.check_value == true) {

                    user = sServiceArray!!.get(i).state
                    user_pref = sb_user.append(user + " ").toString()

                    myList.put(user!!)
                }


            AddListingFragment?.et_space_services?.setText(user_pref)

            notifyDataSetChanged()
        })
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var service_title: TextView? = null
        var service_image: ImageView? = null
        var main_layout: LinearLayout? = null

        init {
            main_layout = view?.findViewById(R.id.main_layout)
            service_image = view?.findViewById(R.id.service_title)
            service_title = view?.findViewById(R.id.service_txt)
        }

    }

    override fun getItemViewType(position: Int): Int {

        if (this.sServiceArray?.get(position)?.check_value == false) {
            return 0
        } else {
            return 1
        }
    }
}
package com.brainplow.ogrespace.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.enums.LayoutType
import com.brainplow.ogrespace.models.StateModel
import com.bumptech.glide.Glide


class StatesAdapter(context: Context?, itemss: ArrayList<StateModel>?) : GenericAdapter<StateModel>(context, itemss) {
    private  var context:Context?=null
    private var items:ArrayList<StateModel>?=null
    private var layoutType:LayoutType?=null
    constructor(context: Context?,items:ArrayList<StateModel>,layoutType:LayoutType):this(context,items){
        this.context=context
        this.items=items
        this.layoutType=layoutType

    }
    override fun setViewHolder(parent: ViewGroup, layoutInflater: LayoutInflater): RecyclerView.ViewHolder {
        val view=layoutInflater.inflate(R.layout.carditems,parent,false)
        return StatesHolder(view)
    }

    override fun onBindData(holder: RecyclerView.ViewHolder, value: StateModel) {
        (holder as StatesHolder).setData(value)
    }
   inner class StatesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var stateImage: ImageView?=null
        private var stateName: TextView? = null
        init {
            stateImage=itemView.findViewById(R.id.state_image)
            stateName=itemView.findViewById(R.id.state_name)
        }
        fun setData(state:StateModel){
            stateName?.setText(state.state)
            Glide.with(context!!)
                .load(Urls.iconStorageUrl + state.icon_image).placeholder(R.drawable.cplaceholder)
                .into(stateImage!!)
        }
    }
}
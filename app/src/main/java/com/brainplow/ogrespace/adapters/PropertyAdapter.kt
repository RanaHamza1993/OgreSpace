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
import com.brainplow.ogrespace.models.PropertyModel
import com.bumptech.glide.Glide

class PropertyAdapter(context: Context?, itemss: ArrayList<PropertyModel>?) : GenericAdapter<PropertyModel>(context, itemss) {
    private  var context:Context?=null
    private var layoutType: LayoutType?=null
    constructor(context: Context?,items:ArrayList<PropertyModel>,layoutType:LayoutType):this(context,items){
        this.context=context
        this.layoutType=layoutType

    }    override fun setViewHolder(parent: ViewGroup, layoutInflater: LayoutInflater): RecyclerView.ViewHolder {
        var view:View?=null
        if(layoutType==LayoutType.LayoutProperties) {
            view = layoutInflater.inflate(R.layout.property_item_layout, parent, false)
        }
        return PropertyHolder(view!!)
    }

    override fun onBindData(holder: RecyclerView.ViewHolder, value: PropertyModel) {
        (holder as PropertyHolder).setData(value)
    }

    inner class PropertyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var propertyImage: ImageView?=null
        private var propertyName: TextView? = null
        private var propertyAddress: TextView? = null
        private var propertyPrice: TextView? = null
        private var propertyType: TextView? = null
        private var propertArea: TextView? = null
        init {
            propertyImage=itemView.findViewById(R.id.p_image)
            propertyName=itemView.findViewById(R.id.p_title)
            propertyAddress=itemView.findViewById(R.id.p_address)
            propertyPrice=itemView.findViewById(R.id.p_price)
            propertyType=itemView.findViewById(R.id.p_type)
            propertArea=itemView.findViewById(R.id.p_area)
        }
        fun setData(property: PropertyModel) {
            Glide.with(context!!)
                .load(property.one_pic).placeholder(R.drawable.cplaceholder)
                .into(propertyImage!!)
            propertyName?.setText(property.property_title)
            propertyAddress?.setText(property.address)
            propertyPrice?.setText("$${property.price}")
            propertyType?.setText("For ${property.post_type}")
            propertArea?.setText("${property.property_area} Sqrf")
        }

    }

}
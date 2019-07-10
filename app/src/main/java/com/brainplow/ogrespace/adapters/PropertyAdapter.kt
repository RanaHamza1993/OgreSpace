package com.brainplow.ogrespace.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.StringRequest
import com.android.volley.toolbox.Volley
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.apputils.Urls.urlAddToFav
import com.brainplow.ogrespace.apputils.Urls.urlDelFav
import com.brainplow.ogrespace.enums.LayoutType
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.models.PropertyModel
import com.bumptech.glide.Glide
import org.json.JSONObject



class PropertyAdapter(context: Context?, itemss: ArrayList<PropertyModel>?) :
    GenericAdapter<PropertyModel>(context, itemss) {
    private var context: Context? = null
    private var layoutType: LayoutType? = null
    private var process: String? = null
    private var itemClickListener:Communicator.IItemDetail?=null
    public fun setItemClickListener(itemClickListener:Communicator.IItemDetail){
        this.itemClickListener=itemClickListener
    }
    constructor(context: Context?, items: ArrayList<PropertyModel>, layoutType: LayoutType, process: String) : this(
        context,
        items
    ) {
        this.context = context
        this.layoutType = layoutType
        this.process = process

    }

    constructor(context: Context?, items: ArrayList<PropertyModel>, layoutType: LayoutType) : this(context, items) {
        this.context = context
        this.layoutType = layoutType

    }

    override fun setViewHolder(parent: ViewGroup, layoutInflater: LayoutInflater): RecyclerView.ViewHolder {
        var view: View? = null
        if (layoutType == LayoutType.LayoutProperties) {
            view = layoutInflater.inflate(R.layout.property_item_layout, parent, false)
        } else if (layoutType == LayoutType.LayoutHorizontalProperties) {
            view = layoutInflater.inflate(R.layout.property_item_horizontal_layout, parent, false)
        }
        return PropertyHolder(view!!)
    }

    override fun onBindData(holder: RecyclerView.ViewHolder, value: PropertyModel) {
        (holder as PropertyHolder).setData(value)
        (holder as PropertyHolder).setClicks(value)
    }

    inner class PropertyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var propertyImage: ImageView? = null
        private var propertyName: TextView? = null
        private var propertyAddress: TextView? = null
        private var propertyPrice: TextView? = null
        private var propertyType: TextView? = null
        private var propertArea: TextView? = null
        private var img_fav: ImageView? = null
        private var top_card_view: CardView? = null

        init {
            propertyImage = itemView.findViewById(R.id.p_image)
            propertyName = itemView.findViewById(R.id.p_title)
            propertyAddress = itemView.findViewById(R.id.p_address)
            propertyPrice = itemView.findViewById(R.id.p_price)
            propertyType = itemView.findViewById(R.id.p_type)
            propertArea = itemView.findViewById(R.id.p_area)
            img_fav = itemView.findViewById(R.id.img_fav)
            top_card_view = itemView.findViewById(R.id.top_card_view)
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

        fun setClicks(value: PropertyModel) {


            img_fav?.setOnClickListener {
                if (process.equals("delete")) {
                    deleteFromFav(value.id)
                } else {
                    addToFav(value.id)
                }
            }

            itemView?.setOnClickListener {
                itemClickListener?.onItemClick(value.id)
            }

    }

    private fun deleteFromFav(id: Int?) {
        var request = object : StringRequest(Request.Method.DELETE, urlDelFav + id, Response.Listener { response ->

            Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()


        }, Response.ErrorListener {
            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                var pref = context?.getSharedPreferences("login", Context.MODE_PRIVATE)
                var token = pref?.getString("token", "abc")
                var map = HashMap<String, String>()
                map.put("Authorization", "JWT " + token)
                return map
            }
        }
        var queue = Volley.newRequestQueue(context)
        queue?.add(request)
    }

    private fun addToFav(id: Int?) {
        var request =
            object : StringRequest(Request.Method.GET, urlAddToFav + id, Response.Listener { response ->
                var obj = JSONObject(response)
                var results = obj.getString("results")
                var favourite = obj.getBoolean("favourite")
                Toast.makeText(context, "Property added to favourites successfully" + id + results, Toast.LENGTH_SHORT)
                    .show()

            }, Response.ErrorListener {
                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    var pref = context?.getSharedPreferences("login", Context.MODE_PRIVATE)
                    var token = pref?.getString("token", "abc")
                    var map = HashMap<String, String>()
                    map.put("Authorization", "JWT " + token)
                    return map
                }
            }
        var queue = Volley.newRequestQueue(context)
        queue?.add(request)
    }

}}
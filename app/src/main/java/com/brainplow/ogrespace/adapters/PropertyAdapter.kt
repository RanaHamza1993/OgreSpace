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
import com.brainplow.ogrespace.activities.MainActivity
import com.brainplow.ogrespace.apputils.Urls
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
    private var favouriteListener: Communicator.IFavourites? = null
    private var favMap = HashMap<String, Int>()

    public fun setFavouriteListener(favouriteListener: Communicator.IFavourites) {
        this.favouriteListener = favouriteListener
    }

    private var itemClickListener: Communicator.IItemDetail? = null
    public fun setItemClickListener(itemClickListener: Communicator.IItemDetail) {
        this.itemClickListener = itemClickListener
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

    constructor(
        context: Context?,
        items: ArrayList<PropertyModel>,
        layoutType: LayoutType,
        favMap: HashMap<String, Int>
    ) : this(context, items) {
        this.context = context
        this.layoutType = layoutType
        this.favMap = favMap
    }

    override fun setViewHolder(parent: ViewGroup, layoutInflater: LayoutInflater): RecyclerView.ViewHolder {
        var view: View? = null
        if (layoutType == LayoutType.LayoutProperties || layoutType == LayoutType.LayoutFavourites) {
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
        private var delFav: ImageView? = null

        init {
            propertyImage = itemView.findViewById(R.id.p_image)
            propertyName = itemView.findViewById(R.id.p_title)
            propertyAddress = itemView.findViewById(R.id.p_address)
            propertyPrice = itemView.findViewById(R.id.p_price)
            propertyType = itemView.findViewById(R.id.p_type)
            propertArea = itemView.findViewById(R.id.p_area)
            img_fav = itemView.findViewById(R.id.img_fav)
            delFav = itemView.findViewById(R.id.del_fav)
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
            checkFavItems(value)
            img_fav?.setOnClickListener {
                val backgroundImageName = img_fav?.getTag().toString()

                if (backgroundImageName === "emptyheart") {
                    img_fav?.setImageResource(R.drawable.fillheart)
                    img_fav?.setTag("fillheart")


                } else if (backgroundImageName === "fillheart") {
                    img_fav?.setImageResource(R.drawable.bottom_heart_home)
                    img_fav?.setTag("emptyheart")


                }

                favouriteListener?.addToFav(value.id)

            }
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(value.id)
            }
            delFav?.setOnClickListener {
                val doWork: () -> Unit = { ->
                    favMap.remove(value.id.toString())
                    removeItem(value)
                    favouriteListener?.deleteFromFav(value.id)
                }
                showCustomAlerDialog(context, "Item Delete", "Are you sure you want to delete this item?",
                    "yes", "no", doWork, {

                    })

            }
        }

        fun checkFavItems(value: PropertyModel) {
            if (layoutType == LayoutType.LayoutFavourites) {
                delFav?.visibility = View.VISIBLE
                img_fav?.setImageResource(R.drawable.fillheart)
            } else {
                delFav?.visibility = View.GONE
                img_fav?.setImageResource(R.drawable.bottom_heart_home)
            }
            if (layoutType != LayoutType.LayoutFavourites) {
                if (MainActivity.favItemsMap.contains(value.id.toString())) {
                    img_fav?.setImageResource(R.drawable.fillheart)
                    img_fav?.setTag("fillheart")


                } else {
                    img_fav?.setImageResource(R.drawable.bottom_heart_home)
                    img_fav?.setTag("emptyheart")

                }
            }

        }


    }

}
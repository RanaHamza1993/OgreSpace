package com.brainplow.ogrespace.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.enums.LayoutType
import com.brainplow.ogrespace.models.CategoriesModel

class CategoriesAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ICategory{
        fun onCatClick(id:Int?,name:String?){}
    }
    private var context: Context? = null
    private var layoutType: LayoutType? = null
    private var catListener: ICategory? = null
    private var catList: ArrayList<CategoriesModel>? = null

    constructor(context:Context?,catList:ArrayList<CategoriesModel>,layoutType: LayoutType):this(){
        this.context=context
        this.catList=catList
        this.layoutType=layoutType
    }
    fun setCategoryListener(catListener:ICategory){
        this.catListener=catListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.states_vertical, parent, false)
        return CategoriesHolder(view)    }

    override fun getItemCount(): Int {

       return catList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CategoriesHolder).setData(position)
    }
    inner class CategoriesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var catImage: ImageView? = null
        private var catName: TextView? = null
        init {
            catImage = itemView.findViewById(R.id.state_image)
            catName = itemView.findViewById(R.id.state_name)
        }
        fun setData(position: Int){
            catImage?.setImageDrawable(context?.resources?.getDrawable(R.drawable.cplaceholder))
            catName?.setText(catList!![position].Property_type)
            itemView.setOnClickListener {
                catListener?.onCatClick(catList!![position].id,catList!![position].Property_type)
            }
        }
    }
}
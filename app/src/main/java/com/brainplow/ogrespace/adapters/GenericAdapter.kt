package com.brainplow.ogrespace.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class GenericAdapter<T>(private val context: Context?, private var items: ArrayList<T>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract fun setViewHolder(parent: ViewGroup, layoutInflater: LayoutInflater): RecyclerView.ViewHolder

    abstract fun onBindData(holder: RecyclerView.ViewHolder, value: T)

    override  fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return setViewHolder(parent, LayoutInflater.from(parent.context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindData(holder, items!![position])
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    fun addItems(savedCardItemz: ArrayList<T>) {
        items = savedCardItemz
        this.notifyDataSetChanged()
    }

    fun getItem(position: Int): T {
        return items!![position]
    }
}
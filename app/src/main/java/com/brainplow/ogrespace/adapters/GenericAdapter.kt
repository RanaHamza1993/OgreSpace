package com.brainplow.ogrespace.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brainplow.ogrespace.R

abstract class GenericAdapter<T>(private val context: Context?, private var items: ArrayList<T>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    open fun showCustomAlerDialog(
        context: Context?,
        title: String?,
        message: String?,
        positve: String?,
        negative: String?,
        doWork: () -> Unit,
        isTrue: (isCheck: Boolean) -> Unit
    ) {
        val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context!!)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setIcon(context.resources.getDrawable(R.drawable.tologo))
        alertDialogBuilder.setPositiveButton(
            "yes"
        ) { arg0, arg1 ->

            doWork()
            isTrue(true)
        }

        alertDialogBuilder.setNegativeButton(
            "No"
        ) { dialog, which ->
            dialog.dismiss()
            isTrue(false)
        }

        val alertDialog = alertDialogBuilder.create()
        // alertDialog.setIcon(R.drawable.tologo)
        alertDialog.show()
        alertDialog.setCancelable(true)

    }

    abstract fun setViewHolder(parent: ViewGroup, layoutInflater: LayoutInflater): RecyclerView.ViewHolder

    open fun onBindData(holder: RecyclerView.ViewHolder, value: T) {}
    open fun onBindData(holder: RecyclerView.ViewHolder, value: T, position: Int) {}
    open fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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

    fun removeItem(position: Int) {
        items?.removeAt(position)
        this.notifyDataSetChanged()

    }
    fun removeItem(obj: T) {
        items?.remove(obj)
        this.notifyDataSetChanged()
    }

}
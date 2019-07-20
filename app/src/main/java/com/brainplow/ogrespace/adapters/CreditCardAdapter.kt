package com.brainplow.ogrespace.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.models.CreditCardModel
import java.util.ArrayList

class CreditCardAdapter() : RecyclerView.Adapter<CreditCardAdapter.ViewHolder>() {


    internal var obj = ArrayList<CreditCardModel>()
    internal var mflag = 0
    internal var context: Context? = null
    internal var updateCardListener: Communicator.ICreditCard? = null
    internal var deleteCardListener: Communicator.ICreditCard? = null

    constructor(context: Context, obj: ArrayList<CreditCardModel>, mflag: Int) : this() {


        this.context = context
        this.obj = obj

        this.mflag = mflag

    }

    fun setUpdateCardListener(updateCardListener: Communicator.ICreditCard) {
        this.updateCardListener = updateCardListener
    }

    fun setDeleteCardListener(deleteCardListener: Communicator.ICreditCard) {
        this.deleteCardListener = deleteCardListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.credit_card_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mflag == 0) {
            holder.name_txt?.text = obj.get(position).nickname
            holder.cardnumber_txt?.text = obj.get(position).cardNumber.toString()
            holder.date_txt?.text = obj.get(position).expiryDate
            if (obj.get(position).default == false)
              //  holder.checkImage?.setImageResource(R.drawable.cross_payment)
                holder.defaultTxt?.visibility=View.GONE
            else {
                holder.defaultTxt?.visibility=View.VISIBLE
                holder.checkImage?.setImageResource(R.drawable.tik_payment)
            }

            holder.updateCard?.setOnClickListener({
                updateCardListener?.updateCreditCard(obj.get(position).id!!)

            }
            )
            holder.deleteCard?.setOnClickListener({

                val alertDialogBuilder = AlertDialog.Builder(context!!)
                alertDialogBuilder.setMessage("Are you sure you want to delete this Payment Method?")
                alertDialogBuilder.setPositiveButton("yes"
                ) { arg0, arg1 ->

                    deleteCardListener?.deleteCreditCard(obj.get(position).id!!)
                    obj.removeAt(position)
                    notifyDataSetChanged()
                }

                alertDialogBuilder.setNegativeButton("No"
                ) { dialog, which -> }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()

            })
        }
    }


    override fun getItemCount(): Int {
        return obj.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var checkImage: ImageView?
        internal var date_txt: TextView?
        internal var name_txt: TextView?
        internal var defaultTxt: LinearLayout?
        internal var cardnumber_txt: TextView?
        internal var updateCard: TextView?
        internal var deleteCard: TextView?

        init {
            checkImage = itemView.findViewById(R.id.default_check)
            defaultTxt = itemView.findViewById(R.id.default_card)
            name_txt = itemView.findViewById(R.id.nick_name)
            cardnumber_txt = itemView.findViewById(R.id.card_number)
            date_txt = itemView.findViewById(R.id.ex_date)
            updateCard = itemView.findViewById(R.id.update_card)
            deleteCard = itemView.findViewById(R.id.delete_card)
        }
    }
}
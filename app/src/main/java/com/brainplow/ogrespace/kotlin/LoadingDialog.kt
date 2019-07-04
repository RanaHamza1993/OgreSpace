package com.brainplow.ogrespace.kotlin

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.brainplow.ogrespace.R
import java.util.*

class LoadingDialog(internal var loading_msg: String, internal var ctx: Context?) {
    internal var dialog: Dialog? = null

    init {
        dialog = Dialog(ctx)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.loadingdialog)
        //   dialog.setCancelable(true);
        dialog!!.setCanceledOnTouchOutside(true)
        val lp = WindowManager.LayoutParams()
        val window = dialog!!.window
        lp.copyFrom(window!!.attributes)
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = lp
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#D7F7F3F3")));
    }

    fun showdialog() {

        val myimg = dialog!!.findViewById<ImageView>(R.id.myimg)

        val r = Random()
        val myr = r.nextInt(6) + 1

        when (myr) {
            1 -> myimg.setImageResource(R.drawable.fidget1)
            2 -> myimg.setImageResource(R.drawable.fidget2)
            3 -> myimg.setImageResource(R.drawable.fidget3)
            4 -> myimg.setImageResource(R.drawable.fidget4)
            5 -> myimg.setImageResource(R.drawable.fidget5)
            6 -> myimg.setImageResource(R.drawable.fidget6)
        }


        val mytext = dialog!!.findViewById<TextView>(R.id.mytxt)
        //mytext.setText(loading_msg+"...");
        //        Animation anim = AnimationUtils.loadAnimation(ctx, R.anim.rotate);
        //        myimg.setAnimation(anim);

        try {
            dialog!!.show()
        }
        catch (exception: WindowManager.BadTokenException){

        }
    }

    fun ishowingg(): Boolean {
        return if (dialog != null) {
            if (dialog!!.isShowing)
                true
            else
                false

        } else
            false
    }

    fun dismisss() {
        dialog!!.dismiss()
    }


}

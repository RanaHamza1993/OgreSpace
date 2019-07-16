package com.brainplow.ogrespace.baseclasses


import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.android.volley.error.VolleyError
import com.brainplow.ogrespace.R
import es.dmoral.toasty.Toasty
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
open class BaseFragment : Fragment() {


    private var mcontext: Context?=null
    private var loadingBar: ProgressDialog? = null
    var customAlerDialog: AlertDialog?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mcontext=context;
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return null
    }

    fun showDialog(title: String, message: String) {
        loadingBar = ProgressDialog(context)
        loadingBar?.setTitle(title)
        loadingBar?.setMessage(message)
        loadingBar?.show()
    }

    fun dismissDialog() {
        if (loadingBar!!.isShowing())
            loadingBar?.dismiss()
    }

    fun showErrorBody(error: VolleyError?):String?{
        var body: String? = null;
        var charset: Charset = Charset.defaultCharset()
        //get status code here
        var statusC = error?.networkResponse?.statusCode.toString();
        //get response body and parse with appropriate encoding
        if (error?.networkResponse?.data != null) {
            try {
                body = String(error.networkResponse.data, Charset.forName("UTF-8"))
                try {
                    val message = JSONObject(body)
                    try{
                        Toasty.error(mcontext!!,message.getString("Message"), Toast.LENGTH_SHORT,true).show()
                    }catch (e: WindowManager.BadTokenException){}

                }catch(e:Exception){
                    try{
                        Toasty.error(mcontext!!,body, Toast.LENGTH_SHORT,true).show()
                    }catch (e: WindowManager.BadTokenException){}

                }

            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace();
            }
        }
        return body
    }

    fun showErrorMsg(error: VolleyError?):String{
        var body: String? = null;
        var message=""
        //get status code here
        var statusC = error?.networkResponse?.statusCode.toString();
        //get response body and parse with appropriate encoding
        if (error?.networkResponse?.data != null) {
            try {
                body = String(error.networkResponse.data, Charset.forName("UTF-8"))
                try {
                    val obj = JSONObject(body)
                    message=obj.getString("Message")

                }catch(e:Exception){
                }

            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace();
            }
        }
        return message
    }
    fun showCustomAlerDialog(context: Context?, title:String?, message:String?, positve:String?, negative:String?, doWork: ()->Unit, isTrue: (isCheck:Boolean)->Unit){
        val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context!!)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setIcon(resources.getDrawable(R.drawable.tologo))
        alertDialogBuilder.setPositiveButton("yes"
        ) { arg0, arg1 ->

            doWork()
            isTrue(true)
        }

        alertDialogBuilder.setNegativeButton("No"
        ) { dialog, which ->
            dialog.dismiss()
            isTrue(false)
        }

        val alertDialog = alertDialogBuilder.create()
        // alertDialog.setIcon(R.drawable.tologo)
        alertDialog.show()
        alertDialog.setCancelable(true)

    }

    fun navigateToFragment(fragment: Fragment,addToBackStack:Boolean=true,tag:String=""){
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.run{
            if(!tag.equals(""))
                replace(R.id.content_frame, fragment,tag)
            else
                replace(R.id.content_frame, fragment)
            if(addToBackStack)
                addToBackStack(null)

            commit()
        }
    }
}

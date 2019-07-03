package com.brainplow.ogrespace.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.error.VolleyError
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.baseclasses.BaseActivity
import com.brainplow.ogrespace.constants.StaticFunctions.emailValidator
import com.brainplow.ogrespace.constants.StaticFunctions.inviteOthers
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.extesnions.showErrorMessage
import com.brainplow.ogrespace.extesnions.showInfoMessage
import com.brainplow.ogrespace.extesnions.showSuccessMessage
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.VolleyService
import com.google.android.material.textfield.TextInputLayout
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_forgot_pass.*
import org.json.JSONObject

class ForgotPassActivity : BaseActivity(),Communicator.IVolleResult {

    override fun notifySuccess(requestType: RequestType?, response: JSONObject?, url: String, netWorkResponse: Int?) {
        if(url.equals(Urls.urlForgotPwd)){
//          //  val a=response
//            //val b=5
            showInfoMessage(response?.getString("message")!!)
        }
    }

    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String) {
        if(url.equals(Urls.urlForgotPwd)){
            showErrorBody(error)
        }
    }
    private var tilForgotpass: TextInputLayout? = null
    private var etFP: EditText? = null
    var volleyService: VolleyService? = null
    private var dialog: Dialog? = null
    private var bSubmit: TextView? = null

    private var toolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)
        setIds()
        setListeners()

    }
    fun setIds(){
        volleyService = VolleyService(this, this.applicationContext)
        toolbar=findViewById(R.id.forgot_pwd_toolbar)
        setSupportActionBar(toolbar)
        activity_name?.setText("Forgot Password")
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        val backArrow = resources.getDrawable(R.drawable.ic_back_white)
        supportActionBar?.setHomeAsUpIndicator(backArrow)
        tilForgotpass = findViewById(R.id.input_layout_r_mail)
        etFP = findViewById(R.id.et_mail)
        bSubmit = findViewById(R.id.bfp_submit)
    }
    fun setListeners(){
        app_logo?.setOnClickListener(){
            onBackPressed()
        }
        invite?.setOnClickListener {
            inviteOthers(this)
        }

        toolbar?.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                onBackPressed()
            }
        })
        bSubmit?.setOnClickListener { view: View? ->

            if (TextUtils.isEmpty(etFP?.text?.trim())) {

              showErrorMessage("Please enter email")
            } else {

                //forgotPasswordAPI()
                val email=etFP?.text.toString()
                val isValid=emailValidator(email)
                if(isValid) {
                    val obj=JSONObject()
                    obj.put("email",email)
                    volleyService?.postDataVolley(RequestType.JsonObjectRequest, Urls.urlForgotPwd, obj, "")
                }
                else
                {
                    Toasty.error(this,"Please Enter a Valid Email", Toast.LENGTH_SHORT,true).show()
                }
            }

        }
    }
}

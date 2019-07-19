package com.brainplow.ogrespace.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.error.VolleyError
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.baseclasses.BaseActivity
import com.brainplow.ogrespace.constants.StaticFunctions.passwordValidator
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.extesnions.showErrorMessage
import com.brainplow.ogrespace.extesnions.showSuccessMessage
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.VolleyService
import es.dmoral.toasty.Toasty
import org.json.JSONObject

class ChangePasswordActivity : BaseActivity(),Communicator.IVolleResult {

    override fun notifySuccess(requestType: RequestType?, response: JSONObject?, url: String, netWorkResponse: Int?) {
        showSuccessMessage("Password changes successfully")
    }

    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {
        showErrorMessage("Password does not change")
        showErrorBody(error)
    }
    private var etcp_oldPass: EditText? = null
    private var etcp_newPass: EditText? = null
    private var etcp_confirmPass: EditText? = null
    var token:String?=null
    var volleyService: VolleyService? = null
    private var bcpSubmit: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setScreenSize()
        setContentView(R.layout.activity_change_password)
        setIds()

        setListeners()
    }

    fun setIds(){
        volleyService = VolleyService(this, this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", "")//"No name defined" is the default value.
        etcp_oldPass = findViewById(R.id.etcp_old_password)
        etcp_newPass = findViewById(R.id.etcp_new_password)
        etcp_confirmPass = findViewById(R.id.etcp_confirm_password)
        bcpSubmit = findViewById(R.id.bcp_submit)

    }
    fun setListeners(){
        bcpSubmit?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val newPass = "${etcp_newPass?.text?.trim()}"
                val confPass = "${etcp_confirmPass?.text?.trim()}"
                //

                val isValid=passwordValidator(newPass)

                if (TextUtils.isEmpty(etcp_oldPass?.text.toString())) {
                 showErrorMessage("Enter current password")
                } else if (TextUtils.isEmpty(etcp_newPass?.text.toString())) {
                  showErrorMessage("Enter new password")
                }else if(etcp_oldPass?.text.toString().equals(etcp_newPass?.text.toString())){
                    showErrorMessage("Current and New password cannot be same")

                }else if(newPass!=confPass){
                   // Toasty.error(this@ChangePasswordActivity,"Password does not match", Toast.LENGTH_SHORT,true).show()
                showErrorMessage("Password does not match")
                } else if(!isValid){
                    Toasty.error(this@ChangePasswordActivity,"Password must be 8 characters long and must include 1 upper case, 1 lower case, 1 number and 1 special character.",
                        Toast.LENGTH_SHORT,true).show()
                }else
                {
                    // til_newPass?.error = null
                    changePasswordAPI()
                }

                //

            }
        })

    }

    fun setScreenSize(){
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width: Int = dm.widthPixels
        val height: Int = dm.heightPixels
        window.setLayout((width * .8).toInt(), (height * .65).toInt())

    }
    private fun changePasswordAPI() {
        val params= JSONObject()
        params.put("currentPassword", "${etcp_oldPass?.text?.trim()}")
        params.put("newPassword", "${etcp_newPass?.text?.trim()}")
        params.put("newPassword2", "${etcp_confirmPass?.text?.trim()}")
        volleyService?.postDataVolley(RequestType.JsonObjectRequest, Urls.urlChangePassword, params, token!!)

    }
}

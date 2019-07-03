package com.brainplow.ogrespace.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.error.VolleyError
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.baseclasses.BaseActivity
import com.brainplow.ogrespace.constants.StaticFunctions
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.extesnions.showErrorMessage
import com.brainplow.ogrespace.extesnions.showSuccessMessage
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.ActivityNavigator
import com.brainplow.ogrespace.kotlin.LoadingDialog
import com.brainplow.ogrespace.kotlin.VolleyService
import com.facebook.CallbackManager
import com.facebook.login.widget.LoginButton
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import kotlin.math.sign

class LoginActivity : BaseActivity(),Communicator.IVolleResult {
    override fun notifySuccess(requestType: RequestType?, response: JSONObject?, url: String, netWorkResponse: Int?) {
        if(url.equals(Urls.urlSignIn)){
            showSuccessMessage("You have successfully logged in to OgreSpace")
            val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            // var user_id=obj.getString("user_id")
            val token = response?.getString("token")
            // editor.putString("user_id",user_id )
            editor.putString("token", token)
            editor.apply()
            val Preferences = getSharedPreferences("current user", Context.MODE_PRIVATE)
            val mEditor = Preferences.edit()
            mEditor.putString("uname", mail.text.trim().toString())
            mEditor.apply()
            ActivityNavigator<MainActivity>(this@LoginActivity,MainActivity::class.java)

            finish()
        }
    }

    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {
        if(url.equals(Urls.urlSignIn)){
           // showErrorBody(error)
            showErrorMessage("Invalid username or password")
        }
    }
    lateinit var mail: EditText
    lateinit var pass: EditText
    lateinit var spinner: LoadingDialog
    var loginButton: LoginButton? = null
    var signInButton: CardView? = null
    var fblogin: LinearLayout? = null
    var emailLayout: TextInputLayout? = null
    var signInBtn: TextView? = null
    var forgotPassword: TextView? = null
    var passLayout: TextInputLayout? = null
    var Role = "U"
    var volleyService: VolleyService? = null
    // var isValidUsername=false
    var loginRoot: RelativeLayout?=null
    var currentUser: SharedPreferences? = null
    var socialLayout: ConstraintLayout?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)
        setIds()
        setListeners()


    }

    fun setIds(){
        volleyService = VolleyService(this, this.applicationContext)
        socialLayout=findViewById(R.id.login_social)
        //fbtxt = findViewById(R.id.fbtxt)
        currentUser = getSharedPreferences("login", Context.MODE_PRIVATE)
        val isfbLogin = currentUser?.getString("social", "")
        loginButton = findViewById(R.id.fb_login)
        fblogin = socialLayout?.findViewById(R.id.fblinear)
        signInBtn = findViewById(R.id.b_login)
        forgotPassword=findViewById(R.id.forgot_pwd)
        emailLayout = findViewById(R.id.input_layout_email)
        passLayout = findViewById(R.id.input_layout_password)
        loginRoot=findViewById(R.id.login_root)
        signInButton = socialLayout?.findViewById(R.id.googlecard)
        mail = findViewById(R.id.l_mail)
        pass = findViewById(R.id.l_password)
        spinner = LoadingDialog("", this)
    }
    fun  setListeners(){
        pass.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                if (s.toString().trim().length > 0)
                    input_layout_password.isPasswordVisibilityToggleEnabled = true
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {


            }

        })

        fblogin?.setOnClickListener({

//            loginButton?.performClick()
//            loginButton?.setReadPermissions("email")


        })

        signInBtn?.setOnClickListener {
            singnIn()
        }
        privacypolicy.setOnClickListener({
            ActivityNavigator<PrivacyPolicyActivity>(this@LoginActivity,PrivacyPolicyActivity::class.java)


        })

        terms_of_use.setOnClickListener({

            ActivityNavigator<TermsOfUseActivity>(this@LoginActivity,TermsOfUseActivity::class.java)

        })

        loginRoot?.setOnClickListener {

            StaticFunctions.hideKeyboard(it,this)
        }
        b_register?.setOnClickListener {
            ActivityNavigator<RegisterActivity>(this@LoginActivity,RegisterActivity::class.java)

        }
        forgotPassword?.setOnClickListener {
            ActivityNavigator<ForgotPassActivity>(this@LoginActivity,ForgotPassActivity::class.java)

        }
    }

    private fun singnIn() {
        val username = mail.text.trim().toString()
        val password = pass.text.trim().toString()
        if (username.isEmpty()) {
            showErrorMessage("Enter username")
            return
        }
        if (password.isEmpty()) {

            showErrorMessage("Enter password")

            return
        }
        val obj=JSONObject()
        obj.put("username",username)
        obj.put("password",password)
        volleyService?.postDataVolley(RequestType.JsonObjectRequest, Urls.urlSignIn, obj, "")
    }

}

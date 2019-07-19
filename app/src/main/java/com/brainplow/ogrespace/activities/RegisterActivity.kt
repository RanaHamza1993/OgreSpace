package com.brainplow.ogrespace.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.error.AuthFailureError
import com.android.volley.error.VolleyError
import com.android.volley.request.StringRequest
import com.badoo.mobile.util.WeakHandler
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.apputils.Urls.urlEmailCheck
import com.brainplow.ogrespace.apputils.Urls.urlSignUp
import com.brainplow.ogrespace.apputils.Urls.urlUserNameCheck
import com.brainplow.ogrespace.baseclasses.BaseActivity
import com.brainplow.ogrespace.constants.StaticFunctions
import com.brainplow.ogrespace.constants.StaticFunctions.emailValidator
import com.brainplow.ogrespace.constants.StaticFunctions.passwordValidator
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.extesnions.showErrorMessage
import com.brainplow.ogrespace.extesnions.showSuccessMessage
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.*
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.HashMap
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterActivity : BaseActivity(),Communicator.IVolleResult {

    override fun notifySuccess(requestType: RequestType?, response: JSONObject?, url: String, netWorkResponse: Int?) {

        if(url.equals(urlSignUp)){
            showSuccessDialog()
            WeakHandler().postDelayed({
                dialog.dismiss()
            },2500)
            spinner?.dismisss()
        }
        if(url.equals(urlEmailCheck)){
            if(netWorkResponse==201)
                input_layout_r_mail.error="Email already exist"
                else if(netWorkResponse==200)
            input_layout_r_mail.error=""
        }
        if(url.equals(urlUserNameCheck)){
            if(netWorkResponse==201)
            input_layout_r_un.error="Username already exist"
            else if(netWorkResponse==200)
            input_layout_r_un.error=""



        }
    }

    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {
        if(url.equals(Urls.urlSignUp)){
            showErrorBody(error)
        }

    }
    lateinit var memail: EditText
    lateinit var pass: EditText
    lateinit var conpass: EditText
    lateinit var name: EditText
    lateinit var fname: EditText
    lateinit var lname: EditText
    var volleyService: VolleyService? = null
    var alreadyMember: TextView? = null
    var privcy_terms: TextView? = null
    var  signup: TextView? = null
    var spinner: LoadingDialog? = null
    var checkTerms: Switch? = null
    private lateinit var dialog: Dialog
    var handler: WeakHandler? = null
    private var toolbar: Toolbar? = null
    var signUpRoot: LinearLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setIds()
        setListeners()
    }

    fun setIds(){
        volleyService = VolleyService(this, this.applicationContext)
        signUpRoot=findViewById(R.id.signup_root)
        toolbar = findViewById(R.id.signup_toolbar)
        setSupportActionBar(toolbar);
        activity_name?.setText("Sign Up")
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        val backArrow = resources.getDrawable(R.drawable.ic_back_white)
        supportActionBar?.setHomeAsUpIndicator(backArrow)
        handler = WeakHandler()
        privcy_terms = findViewById(R.id.privcy_terms)
        alreadyMember = findViewById(R.id.already_member)
//        alreadyMember?.setOnClickListener({
//
//            //            var intent=Intent(this@RegisterActivity,LoginActivity::class.java)
////            startActivity(intent)
////            finish()
//            onBackPressed()
//
//        })
        memail = findViewById(R.id.et_mail)
        pass = findViewById(R.id.et_password)
        conpass = et_confirmPassword
        name = findViewById(R.id.et_userName)
        fname = et_firstName
        lname = et_lastName
        checkTerms = findViewById(R.id.terms_and_privacy)
        signup= findViewById(R.id.b_register)
        spinner = LoadingDialog("", this)
        SSL.sslCertificates()


        val alreadySpanable= SpannableString("Already have an Account? Sign In")
        val ss = SpannableString("By clicking on the Sign Up button, you understand and agree with the Terms of Use and Privacy Policy.")
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                openTerms()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.setUnderlineText(false)
            }
        }
        val privacyClick = object : ClickableSpan() {
            override fun onClick(textView: View) {
                openPrivacy()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.setUnderlineText(false)
            }
        }

        val alreadyClickableSpan=object: ClickableSpan(){
            override fun onClick(widget: View) {
                onBackPressed()
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.setUnderlineText(false)
            }
        }
        ss.setSpan(clickableSpan, 68, 81, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(privacyClick, 86, 100, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        alreadySpanable.setSpan(alreadyClickableSpan, 25, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        alreadySpanable.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 25, 32, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 68, 81, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 86, 100, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        var fcs = ForegroundColorSpan(resources.getColor(R.color.blue))
        var fc = ForegroundColorSpan(resources.getColor(R.color.blue))
        var fccs = ForegroundColorSpan(resources.getColor(R.color.blue))
        ss.setSpan(fcs, 68, 81, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        ss.setSpan(fc, 86, 100, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        alreadySpanable.setSpan(fccs,25,32, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        privcy_terms?.setText(ss)
        privcy_terms?.setMovementMethod(LinkMovementMethod.getInstance())
        privcy_terms?.setHighlightColor(Color.TRANSPARENT)
        alreadyMember?.setText(alreadySpanable)
        alreadyMember?.setMovementMethod(LinkMovementMethod.getInstance())
        alreadyMember?.setHighlightColor(Color.TRANSPARENT)

    }
    fun setListeners(){
        app_logo?.setOnClickListener({
            onBackPressed()
        })
        invite?.setOnClickListener {
            StaticFunctions.inviteOthers(this)
        }
        toolbar?.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                onBackPressed()
            }
        })

        pass.setCustomSelectionActionModeCallback(object: android.view.ActionMode.Callback {
            override fun onActionItemClicked(mode: android.view.ActionMode?, item: MenuItem?): Boolean {
                return false
            }

            override fun onCreateActionMode(mode: android.view.ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onPrepareActionMode(mode: android.view.ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: android.view.ActionMode?) {

            }


        })
        conpass.setCustomSelectionActionModeCallback(object: android.view.ActionMode.Callback {
            override fun onActionItemClicked(mode: android.view.ActionMode?, item: MenuItem?): Boolean {
                return false
            }

            override fun onCreateActionMode(mode: android.view.ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onPrepareActionMode(mode: android.view.ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: android.view.ActionMode?) {

            }


        })
        pass.setLongClickable(false)
        pass.setTextIsSelectable(false)
        conpass.setLongClickable(false)
        conpass.setTextIsSelectable(false)
        signUpRoot?.setOnClickListener {

            StaticFunctions.hideKeyboard(it,this)
        }
        pass.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                input_layout_r_pass.helperText="Password must be 8 characters long and must include 1 upper case, 1 lower case, 1 number and 1 special character"

            }else
                input_layout_r_pass.helperText=""
        }
        memail.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){

            }else {
                val isValid= emailValidator(memail.text.toString())
                if (memail.text.length > 1&&isValid)
                    checkEmailExistence(memail.text.toString())
            }
        }
        name.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){

            }else
                if(name.text.length>0)
                checkUserNameExistence(name.text.toString())
        }
        conpass.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus)
                input_layout_c_pass.helperText=""
                //input_layout_c_pass.helperText="Password must be 8 characters long and must include 1 upper case, 1 lower case, 1 number and 1 special character"

            else
                input_layout_c_pass.helperText=""
        }
        signup?.setOnClickListener {


            signupAPI()
        }


    }
    private fun openTerms() {
        ActivityNavigator<TermsOfUseActivity>(this@RegisterActivity,TermsOfUseActivity::class.java)
    }

    private fun openPrivacy() {
        ActivityNavigator<PrivacyPolicyActivity>(this@RegisterActivity,PrivacyPolicyActivity::class.java)
    }
    private fun signupAPI() {


        val username = name.text.trim().toString()
        val firstname = fname.text.trim().toString()
        val lastname = lname.text.trim().toString()
        val password = pass.text.trim().toString()
        val confirmPass = conpass.text.trim().toString()
        val mail = memail.text.trim().toString()
        val isValid = emailValidator(mail)
        val isPwdValid = passwordValidator(password)

        //   val isValid=passwordValidator(password)
        if (firstname.isEmpty()) {

            // fname.error = "Enter First Name"
            showErrorMessage("Enter first name")
            return
        }
        if(firstname.length<2){
            showErrorMessage("First name must be between 2 and 64 characters")
            return
        }
        if (lastname.isEmpty()) {

            // lname.error = "Enter Last Name"
            showErrorMessage("Enter last Name")
            return
        }
        if (lastname.length<2) {
            showErrorMessage("Last name must be between 2 and 64 characters")
            return
        }
        if (username.isEmpty()) {

            //  name.error = "Enter Username"
            showErrorMessage("Enter username")
            return
        }
        if (username.length<3) {
            showErrorMessage("Username must be between 3 and 64 characters")
            return
        }

        if (mail.isEmpty()) {
            // var isValid = emailValidator(mail)
            // memail.error = "Enter a valid email address"
            showErrorMessage("Enter a valid email address")
            return
        }
        if (!isValid) {

            // memail.error = "Enter a valid email address"
            showErrorMessage("Enter a valid email address")
            return
        }
        if (password.isEmpty()) {
            // input_layout_r_pass.isPasswordVisibilityToggleEnabled = false

            //  pass.error = "Enter a Password"
            showErrorMessage("Enter password")
            return
        }

        if (confirmPass.isEmpty()) {
            //   input_layout_c_pass.isPasswordVisibilityToggleEnabled = false
            //  conpass.error = "Enter Password, again"
            showErrorMessage("Enter confirm password")

            return
        }
        if (password != confirmPass) {

            //input_layout_c_pass.isPasswordVisibilityToggleEnabled=false
            // conpass.error = "Passwords are not same"
            showErrorMessage("Password does not match")
            // Toasty.error(this, "Passwords are not same", Toast.LENGTH_SHORT, true).show()
            return
        }
        if (!isPwdValid) {

            //input_layout_c_pass.isPasswordVisibilityToggleEnabled=false
            // conpass.error = "Passwords are not same"
            showErrorMessage("Password must be 8 characters long and must include 1 upper case, 1 lower case, 1 number and 1 special character.")
            // Toasty.error(this, "Passwords are not same", Toast.LENGTH_SHORT, true).show()
            return
        }
        if (!checkTerms!!.isChecked) {
            showErrorMessage("Please agree to Terms of Use and Privacy Policy")
            return
        }
        spinner?.showdialog()
        var mStatusCode = 0
        val obj=JSONObject()
        obj.put("email",mail)
        obj.put("username",username)
        obj.put("password",password)
        obj.put("fname",firstname)
        obj.put("lname",lastname)
        obj.put("mobile","")
        obj.put("state","")
        obj.put("country","")
        obj.put("city","")
        obj.put("zip","")
        obj.put("pic","")
        obj.put("address","")
        obj.put("user_role","U")

        volleyService?.postDataVolley(RequestType.JsonObjectRequest, Urls.urlSignUp, obj, "")

    }
    private fun showSuccessDialog() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.account_verification_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }

    fun checkEmailExistence(email:String?){
        volleyService?.postDataVolley(RequestType.JsonObjectRequest, Urls.urlEmailCheck, JSONObject().put("email",email), "")
    }
    fun checkUserNameExistence(userName:String?){
        volleyService?.postDataVolley(RequestType.JsonObjectRequest, Urls.urlUserNameCheck, JSONObject().put("username",userName), "")
    }

}

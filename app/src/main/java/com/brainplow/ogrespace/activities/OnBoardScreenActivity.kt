package com.brainplow.ogrespace.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.kotlin.ActivityNavigator
import com.brainplow.ogrespace.kotlin.LoadingDialog
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.DefaultSliderView
import com.facebook.login.widget.LoginButton
import kotlinx.android.synthetic.main.activity_on_board_screen.*
import java.util.HashMap

class OnBoardScreenActivity : AppCompatActivity() {

    lateinit var slider: SliderLayout
    var loginButton: LoginButton? = null
    var signInButton: CardView? = null
    var fblogin: LinearLayout? = null
    var Role = "U"
    var socialLayout: ConstraintLayout?=null
    var privcy_terms: TextView? = null
    lateinit var spinner: LoadingDialog
    var currentUser: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board_screen)
       setIds()
        setListeners()
    }
    private fun openTerms() {
       ActivityNavigator<TermsOfUseActivity>(this@OnBoardScreenActivity,TermsOfUseActivity::class.java)
    }

    private fun openPrivacy() {
        ActivityNavigator<PrivacyPolicyActivity>(this@OnBoardScreenActivity,PrivacyPolicyActivity::class.java)
    }
    private fun onBoardSlider() {
        slider = findViewById(R.id.banner_slider1)
        slider.getPagerIndicator().setDefaultIndicatorColor(getResources().getColor(R.color.Red), getResources().getColor(R.color.gray));


//
        val url_maps = HashMap<String, Int>()
        url_maps["1"] = R.drawable.onboard1
        url_maps["2"] = R.drawable.onboard2
        url_maps["3"] = R.drawable.onboard3
        url_maps["4"] = R.drawable.onboard5
        url_maps["5"] = R.drawable.onboard5

//        val url_maps = HashMap<String, Int>()
//        url_maps["Electronics"] = R.drawable.onboard1
//        url_maps["Beats Audio"] = R.drawable.onboard2
//        url_maps["Apple Mackbook Pro"] = R.drawable.onboard3
//        url_maps["Home Appliances"] = R.drawable.onboard4

        for (name in url_maps.keys) {
            val textSliderView = DefaultSliderView(this)
            // initialize a SliderLayout
            textSliderView.image(url_maps[name]!!)
                .setScaleType(BaseSliderView.ScaleType.CenterCrop)

            slider.setBackgroundColor(Color.TRANSPARENT)
            slider.setPresetTransformer(SliderLayout.Transformer.Default)
            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
            slider.setCustomAnimation(DescriptionAnimation())
            slider.setDuration(5000)

            //add your extra information
            textSliderView.bundle(Bundle())
            textSliderView.bundle
                .putString("extra", name)
            slider.addSlider(textSliderView)

        }
    }
    fun setIds(){
        socialLayout=findViewById(R.id.splash_social)
        spinner = LoadingDialog("", this)
        privcy_terms = findViewById(R.id.privcy_terms)
        //  fbtxt = findViewById(R.id.fbtxt)
        currentUser = getSharedPreferences("login", Context.MODE_PRIVATE)
        val isfbLogin = currentUser?.getString("social", "")

    }
    fun setListeners(){
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
        ss.setSpan(clickableSpan, 68, 81, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(privacyClick, 86, 100, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 68, 81, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 86, 100, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val fcs = ForegroundColorSpan(resources.getColor(R.color.blue))
        val fc = ForegroundColorSpan(resources.getColor(R.color.blue))
        ss.setSpan(fcs, 68, 81, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        ss.setSpan(fc, 86, 100, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        privcy_terms?.setText(ss)
        privcy_terms?.setMovementMethod(LinkMovementMethod.getInstance())
        privcy_terms?.setHighlightColor(Color.TRANSPARENT)
        onBoardSlider()
        signin.setOnClickListener {
            ActivityNavigator<LoginActivity>(this@OnBoardScreenActivity,LoginActivity::class.java)

        }
        signup?.setOnClickListener {
            ActivityNavigator<RegisterActivity>(this@OnBoardScreenActivity,RegisterActivity::class.java)

        }
    }

}

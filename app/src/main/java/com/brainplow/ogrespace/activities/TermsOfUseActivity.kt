package com.brainplow.ogrespace.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.constants.StaticFunctions.inviteOthers
import kotlinx.android.synthetic.main.activity_terms_of_use.*

class TermsOfUseActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_of_use)
        name?.setText("Terms of Use")
        invite?.setOnClickListener {
            inviteOthers(this)
        }
        app_logo?.setOnClickListener({
            onBackPressed()
        })
        toolbar=findViewById(R.id.terms_toolbar)
        setSupportActionBar(toolbar)
        // supportActionBar?.setTitle("Terms of Use");
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val backArrow = resources.getDrawable(R.drawable.ic_back_white)
        supportActionBar?.setHomeAsUpIndicator(backArrow)
        toolbar?.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                onBackPressed()
            }
        })
    }
}

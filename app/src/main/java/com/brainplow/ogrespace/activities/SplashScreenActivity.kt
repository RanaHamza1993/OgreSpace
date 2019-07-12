package com.brainplow.ogrespace.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.badoo.mobile.util.WeakHandler
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.kotlin.ConnectionDetector
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slpash_screen)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val connectionDetector = ConnectionDetector(this)
        Handler().postDelayed({
            val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", "")//"No name defined" is the default value.

            if (connectionDetector.isconnected()) {
                if (!token.equals("")) {
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)
                    this@SplashScreenActivity.finish()

                } else {

                    val intent = Intent(this@SplashScreenActivity, OnBoardScreenActivity::class.java)

                    startActivity(intent)

                    this@SplashScreenActivity.finish()
                }
            } else if (!connectionDetector.isconnected()) {
                Toasty.info(this@SplashScreenActivity, "Please connect to Internet", Toast.LENGTH_SHORT, true).show()

                //      Toast.makeText(SplashScreen.this,"No Net connected pleasse try again",Toast.LENGTH_SHORT).show();

                val snk =
                    Snackbar.make(findViewById(R.id.splash), "Please connect to Internet.", Snackbar.LENGTH_INDEFINITE)
                        .setAction(
                            "Retry"
                        ) {
                            finish()
                            startActivity(intent)
                        }
                snk.show()
            }
        }, 3000)


    }
}

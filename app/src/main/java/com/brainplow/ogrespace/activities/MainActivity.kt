package com.brainplow.ogrespace.activities

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.baseclasses.BaseActivity
import com.brainplow.ogrespace.fragments.HomeFragment
import com.brainplow.ogrespace.kotlin.ActivityNavigator
import com.facebook.login.LoginManager
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*

class MainActivity : BaseActivity() {

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navigationView: NavigationView
    var rootView: DrawerLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setIds()
        botnav()
        setListeners()
    }

    fun setIds(){
        bottomNavigationView = findViewById(R.id.navigation)
        navigationView=findViewById(R.id.nav_view)
        rootView = findViewById(R.id.drawer_layout)
    }

    fun setListeners(){
        bottomNavigationView.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.bottom_home -> {
                        nav_view.getMenu().findItem(R.id.nav_home).setChecked(true)
                        toolbar?.visibility = View.VISIBLE

                        moveToHome()

                        val fragment = HomeFragment()
                        val fragment2 = supportFragmentManager.findFragmentById(R.id.content_frame)
                        if (fragment2 is HomeFragment) {
                            //   Toast.makeText(this@MainActivity, "Visible", Toast.LENGTH_LONG)

                        } else {

                            val fragmentTransaction = supportFragmentManager.beginTransaction()
                            fragmentTransaction.run{
                                replace(R.id.content_frame, fragment)
                                //  isBackButtonEnabled(false)
                                commit()
                            }

                        }

                    }

                }
                return true
            }


        })
        navigationView.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId){
                    R.id.logout ->{
                        logOut()
                    }
                    R.id.established_contact ->{
                        ActivityNavigator<MapsActivity>(this@MainActivity,MapsActivity::class.java)
                    }
                }
                drawer_layout.closeDrawer(GravityCompat.START)
                return true
            }

        })

    }
    fun botnav() {

        val fragment = HomeFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.run{
            replace(R.id.content_frame, fragment)
            commit()
        }
        bottomNavigationView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        val menuView = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        bottomNavigationView.getMenu()?.findItem(R.id.bottom_home)?.isChecked = true
        for (i in 0 until menuView.getChildCount()) {
            val iconView: View? = menuView.getChildAt(i).findViewById(R.id.icon)
            val layoutParams: ViewGroup.LayoutParams = iconView?.getLayoutParams()!!
            val displayMetrics = resources.displayMetrics
            layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, displayMetrics).toInt()
            layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, displayMetrics).toInt()
            iconView.setLayoutParams(layoutParams)
        }


    }

    fun logOut(){
        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", "")
        editor.apply()
        val Preferences = getSharedPreferences("current user", Context.MODE_PRIVATE)
        val mEditor = Preferences.edit()
        mEditor.putString("uname", "noname")
        mEditor.apply()

        Toasty.info(this, "Logout SuccessFully!", Toast.LENGTH_SHORT, true).show()

        ActivityNavigator<LoginActivity>(this@MainActivity,LoginActivity::class.java)

        finish()


    }
            fun moveToHome() {

                var count = supportFragmentManager.backStackEntryCount

                while (count > 0) {
                    //    val first = supportFragmentManager.getBackStackEntryAt(0)
                    //              supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)                          //  fragmentTransaction.addToBackStack("HomeFragment")

                    supportFragmentManager.popBackStackImmediate()
                    count--
                }

            }
}

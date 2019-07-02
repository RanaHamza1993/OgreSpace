package com.brainplow.ogrespace.activities

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.baseclasses.BaseActivity
import com.brainplow.ogrespace.constants.StaticFunctions
import com.brainplow.ogrespace.fragments.HomeFragment
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.ActivityNavigator
import com.facebook.login.LoginManager
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*

class MainActivity : BaseActivity(),Communicator.IActionBar,Communicator.IBottomBar {
    override fun isBottomVisible(isVisible: Boolean) {
        if (isVisible) {


            bottomNavigationView.visibility = View.VISIBLE
           // floatingButton?.visibility = View.VISIBLE
        } else {

            bottomNavigationView.visibility = View.GONE
            //floatingButton?.visibility = View.GONE
        }
    }

    override fun toolbarColor(isWhite: Boolean) {
        if (isWhite) {
            toggle?.getDrawerArrowDrawable()?.setColor(getResources().getColor(R.color.gray))
            toggle!!.syncState()
            //  optionmenu?.findItem(R.id.tinvite)?.icon = resources.getDrawable(R.drawable.invitehome)
            toolbar?.background = resources.getDrawable(R.drawable.search_background)

        } else {
            //   optionmenu?.findItem(R.id.tinvite)?.icon = resources.getDrawable(R.drawable.inviteother)
            val backArrow = resources.getDrawable(R.drawable.ic_back_white)
            supportActionBar?.setHomeAsUpIndicator(backArrow)
            toolbar?.background = resources.getDrawable(R.drawable.toolbar_background)
        }

    }

    override fun toolbarBackground(isWhite: Boolean) {
        if (isWhite) {
            toolbar?.background = resources.getDrawable(R.drawable.search_background)
            //     optionmenu?.findItem(R.id.tinvite)?.icon = resources.getDrawable(R.drawable.invitehome)
            //  invite?.setTextColor(R.color.grey)
        } else {
            //   optionmenu?.findItem(R.id.tinvite)?.icon = resources.getDrawable(R.drawable.inviteother)
            toolbar?.background = resources.getDrawable(R.drawable.toolbar_background)
            val backArrow = resources.getDrawable(R.drawable.ic_back_white)
            supportActionBar?.setHomeAsUpIndicator(backArrow)
            //invite?.setTextColor(R.color.grey)
        }
    }
    override fun actionBarListener(title: String) {
        if (title.equals("Home")) {
            supportActionBar?.setDisplayShowTitleEnabled(false)
            tolLogo?.setOnClickListener({
                val fragment2 = supportFragmentManager.findFragmentById(R.id.content_frame)
                if (fragment2 is HomeFragment) {
                } else
                    moveToHome()
            })
        } else {
            supportActionBar?.setDisplayShowTitleEnabled(true)
            appLogo?.setOnClickListener() { it: View ->
                moveToHome()
            }
            fragmentName?.text = title
        }
    }

    override fun isBackButtonEnabled(isEnabled: Boolean) {
        if (isEnabled) {

            // istrue=true
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            mydrawer?.visibility = View.GONE
            drawer_layout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            mydrawer?.visibility = View.VISIBLE
            // toggle!!.setDrawerIndicatorEnabled(true);
            toggle!!.syncState()
            drawer_layout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

        }
    }

    override fun isSearchVisible(isVisible: Boolean) {
        if (isVisible) {
            searchLinear?.visibility = View.VISIBLE
            //  fragmentName?.visibility=View.GONE
            //   appLogo?.visibility=View.GONE
            // globalInvite?.visibility=View.GONE
            secondary_linear?.visibility = View.GONE
        } else {
            searchLinear?.visibility = View.GONE
            //    fragmentName?.visibility=View.VISIBLE
            //  appLogo?.visibility=View.VISIBLE
            //   globalInvite?.visibility=View.VISIBLE
            secondary_linear?.visibility = View.VISIBLE
        }
    }
    var mydrawer: ImageView? = null
    var searchLinear: LinearLayout? = null
    var secondary_linear: LinearLayout? = null
    var globalInvite: TextView? = null
    var invite: TextView? = null

    var toggle: ActionBarDrawerToggle? = null
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navigationView: NavigationView
    var rootView: DrawerLayout? = null
    var fragmentName: TextView? = null
    var appLogo: ImageView? = null
    var tolLogo: ImageView? = null
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
        fragmentName = findViewById(R.id.fragmentName)
        appLogo = findViewById(R.id.app_logo)
        tolLogo = findViewById(R.id.tologo)
        mydrawer = findViewById(R.id.myhamburger)
        toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle!!)
        toggle?.getDrawerArrowDrawable()?.setColor(getResources().getColor(R.color.gray))
        toggle?.isDrawerIndicatorEnabled = false
        toggle?.syncState()
        toolbar?.setNavigationIcon(null)
        mydrawer?.setOnClickListener({
            drawer_layout.openDrawer(Gravity.LEFT)
        })
        secondary_linear = findViewById(R.id.secondry_linear)
        globalInvite = findViewById(R.id.global_invite)
        invite = findViewById(R.id.tv_topbar_invite)
        searchLinear = findViewById(R.id.toolbar_li)
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

        invite?.setOnClickListener(){
            StaticFunctions.inviteOthers(this)
        }
        globalInvite?.setOnClickListener(){
            StaticFunctions.inviteOthers(this)
        }
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
        bottomNavigationView.getMenu().findItem(R.id.bottom_home)?.isChecked = true
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

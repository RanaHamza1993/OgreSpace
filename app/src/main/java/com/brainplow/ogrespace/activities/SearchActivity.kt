package com.brainplow.ogrespace.activities

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.baseclasses.BaseActivity
import com.brainplow.ogrespace.constants.StaticFunctions
import com.brainplow.ogrespace.fragments.HomeFragment
import com.brainplow.ogrespace.fragments.SearchFragment
import com.brainplow.ogrespace.interfaces.Communicator
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_search_bar.*

class SearchActivity : BaseActivity(), Communicator.IActionBar {

    override fun toolbarColor(isWhite: Boolean) {
        if (isWhite) {

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
                }
            })
        } else {
            supportActionBar?.setDisplayShowTitleEnabled(true)
//            appLogo?.setOnClickListener() { it: View ->
//                moveToHome()
//            }
            fragmentName?.text = title
        }
    }

    override fun isBackButtonEnabled(isEnabled: Boolean) {
        this.isBackEnabled = isEnabled
        if (isEnabled) {

            // istrue=true
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
            drawer_layout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(false)
            // toggle!!.setDrawerIndicatorEnabled(true);
//            toggle!!.syncState()
//            drawer_layout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

        }
    }

    override fun isSearchVisible(isVisible: Boolean) {
        this.isSearchEnable = isVisible
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
    var isBackEnabled: Boolean = false
    var isSearchEnable: Boolean = false
    var searchLinear: LinearLayout? = null
    var secondary_linear: LinearLayout? = null
    var globalInvite: TextView? = null
    var invite: TextView? = null
    var toolbar: Toolbar? = null
    var fragmentName: TextView? = null
    var appLogo: ImageView? = null
    var tolLogo: ImageView? = null
    var headerBack: ImageView? = null
    var rootView: DrawerLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setIds()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        navigateToFragment(SearchFragment(),false)
    }
    fun setIds(){
        val view: View = nav_view.getHeaderView(0)
        headerBack = view.findViewById(R.id.header_back)
        toolbar = findViewById(R.id.toolbar)
        fragmentName = findViewById(R.id.fragmentName)
        appLogo = findViewById(R.id.app_logo)
        tolLogo = findViewById(R.id.tologo)
        rootView = findViewById(R.id.drawer_layout)
        toolbar?.setNavigationIcon(null)

        secondary_linear = findViewById(R.id.secondry_linear)
        globalInvite = findViewById(R.id.global_invite)
        invite = findViewById(R.id.tv_topbar_invite)
        searchLinear = findViewById(R.id.toolbar_li)
        setSupportActionBar(toolbar)
    }

    fun setListeners(){
        search_back?.setOnClickListener {
            onBackPressed()
        }
        toolbar?.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                // perform whatever you want on back arrow click
                if (isBackEnabled) {

                    val r = Rect()
                    rootView!!.getWindowVisibleDisplayFrame(r)
                    val screenHeight = rootView!!.getRootView().getHeight()


                    // r.bottom is the position above soft keypad or device button.
                    // if keypad is shown, the r.bottom is smaller than that before.
                    val keypadHeight = screenHeight - r.bottom;

                    //            Log.d("KeyBoard", "keypadHeight = " + keypadHeight)

                    if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                        // keyboard is opened

                        val inputManager = this@SearchActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.hideSoftInputFromWindow(this@SearchActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)

                    } else {


                        onBackPressed()

                    }


                }

//             else if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//                 drawer_layout.closeDrawer(GravityCompat.START)
//
//
//             }
                else {
                    drawer_layout.openDrawer(Gravity.LEFT)


                }


            }

        })
        invite?.setOnClickListener(){
            StaticFunctions.inviteOthers(this)
        }
        globalInvite?.setOnClickListener(){
            StaticFunctions.inviteOthers(this)
        }

    }
}

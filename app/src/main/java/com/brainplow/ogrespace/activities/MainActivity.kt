package com.brainplow.ogrespace.activities

import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import com.android.volley.error.VolleyError
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.apputils.Urls.urlGetFavItems
import com.brainplow.ogrespace.baseclasses.BaseActivity
import com.brainplow.ogrespace.constants.StaticFunctions
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.fragments.*
import com.brainplow.ogrespace.fragments.ContactUs
import com.brainplow.ogrespace.fragments.HomeFragment
import com.brainplow.ogrespace.fragments.ProfileFragment
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.ActivityNavigator
import com.brainplow.ogrespace.kotlin.VolleyService
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : BaseActivity(), Communicator.IActionBar, Communicator.IBottomBar, Communicator.IVolleResult {

    override fun notifySuccess(requestType: RequestType?, response: JSONObject?, url: String, netWorkResponse: Int?) {
        val array = response?.getJSONArray("results")
        for (i in 0 until array!!.length()) {
            val itemId = array.getJSONObject(i)?.getInt("Property_id")?.toString()
            favItemsMap.put(itemId!!, itemId.toInt())
        }
    }

    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {

    }

    companion object {
        var favItems = MutableLiveData<Int>()
        var favItemsMap = HashMap<String, Int>()
    }

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

    override fun backArrow(isWhite: Boolean) {
        if (isWhite) {
            val backArrow = resources.getDrawable(R.drawable.ic_back_white)
            supportActionBar?.setHomeAsUpIndicator(backArrow)
            toolbar?.background = resources.getDrawable(R.drawable.toolbar_background)


        } else {

//            supportActionBar?.setDisplayHomeAsUpEnabled(true)
//            mydrawer?.visibility = View.GONE
            drawer_layout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
//            toolbar?.background = resources.getDrawable(R.drawable.search_background)
            toolbar?.background = resources.getDrawable(R.drawable.search_background)
            val backArrow = resources.getDrawable(R.drawable.ic_back)
            supportActionBar?.setHomeAsUpIndicator(backArrow)
            // val backArrow = resources.getDrawable(R.drawable.ic_back)
            //supportActionBar?.setHomeAsUpIndicator(backArrow)


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
        if (title.equals("Home") || title.equals("Search")) {
            this.fragmentTitle = title
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
        this.isBackEnabled = isEnabled
        if (isEnabled) {

            // istrue=true
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
            mydrawer?.visibility = View.GONE
            drawer_layout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            getSupportActionBar()!!.setDisplayHomeAsUpEnabled(false)
            mydrawer?.visibility = View.VISIBLE
            // toggle!!.setDrawerIndicatorEnabled(true);
            toggle!!.syncState()
            drawer_layout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

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

    var volleyService: VolleyService? = null
    var isBackEnabled: Boolean = false
    var isBottomEnabled: Boolean = false
    var isSearchEnable: Boolean = false
    var mydrawer: ImageView? = null
    var searchLinear: LinearLayout? = null
    var secondary_linear: LinearLayout? = null
    var globalInvite: TextView? = null
    var invite: TextView? = null
    var toolbar: Toolbar? = null
    var toggle: ActionBarDrawerToggle? = null
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navigationView: NavigationView
    var rootView: DrawerLayout? = null
    var fragmentName: TextView? = null
    var fragmentTitle: String? = null
    var appLogo: ImageView? = null
    var tolLogo: ImageView? = null
    var headerBack: ImageView? = null
    var token: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        token = sharedPreferences!!.getString("token", "JWT")
        setIds()
        volleyRequests()
        botnav()
        setListeners()
    }

    fun volleyRequests() {
        volleyService!!.getDataVolley(RequestType.JsonObjectRequest, urlGetFavItems, token!!)
    }

    fun setIds() {
        volleyService = VolleyService(this, this)
        val view: View = nav_view.getHeaderView(0)
        headerBack = view.findViewById(R.id.header_back)
        toolbar = findViewById(R.id.toolbar)
        bottomNavigationView = findViewById(R.id.navigation)
        navigationView = findViewById(R.id.nav_view)
        rootView = findViewById(R.id.drawer_layout)
        fragmentName = findViewById(R.id.fragmentName)
        appLogo = findViewById(R.id.app_logo)
        tolLogo = findViewById(R.id.tologo)
        mydrawer = findViewById(R.id.myhamburger)
        toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle!!)
        toggle?.getDrawerArrowDrawable()?.setColor(getResources().getColor(R.color.gray))
        toggle?.isDrawerIndicatorEnabled = false
        toggle?.syncState()
        toolbar?.setNavigationIcon(null)
        mydrawer?.setOnClickListener() {
            drawer_layout.openDrawer(Gravity.LEFT)
        }
        secondary_linear = findViewById(R.id.secondry_linear)
        globalInvite = findViewById(R.id.global_invite)
        invite = findViewById(R.id.tv_topbar_invite)
        searchLinear = findViewById(R.id.toolbar_li)
        setSupportActionBar(toolbar)
    }

    fun setListeners() {
        bottomNavigationView.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
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
                            fragmentTransaction.run {
                                replace(R.id.content_frame, fragment)
                                //  isBackButtonEnabled(false)
                                commit()
                            }

                        }

                    }
                    R.id.bottom_search ->{
                       // navigateToFragment(SearchFragment())
                        val f=supportFragmentManager.findFragmentByTag("Search")

                        if(f==null){
                           navigateToFragment(SearchFragment(),true,"Search")
                            } else{

                        }
                    }
                    R.id.bot_save -> {
                        navigateToFragment(MyFavFragment())
                    }
                    R.id.bot_profile -> {
                        navigateToFragment(ProfileFragment())
                    }

                }
                return true
            }


        })
        navigationView.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.logout -> {
                        logOut()
                    }
                    R.id.established_contact -> {
                        // ActivityNavigator<MapsActivity>(this@MainActivity,MapsActivity::class.java)
                        navigateToFragment(ContactUs())
                    }
                    R.id.nav_service -> {
                        // ActivityNavigator<MapsActivity>(this@MainActivity,MapsActivity::class.java)
                        navigateToFragment(OgreasService())
                    }
                    R.id.nav_terms_condition -> {
                        ActivityNavigator<TermsOfUseActivity>(this@MainActivity, TermsOfUseActivity::class.java)

                    }
                    R.id.nav_privacy_policy -> {
                        ActivityNavigator<PrivacyPolicyActivity>(this@MainActivity, PrivacyPolicyActivity::class.java)

                    }
                    R.id.nav_profile -> {
                        navigateToFragment(ProfileFragment())
                    }
                    R.id.nav_fav -> {
                        navigateToFragment(MyFavFragment())
                    }
                    R.id.nav_faqs -> {
                        //navigateToFragment(TestFragment())
                    }
                }
                drawer_layout.closeDrawer(GravityCompat.START)
                return true
            }

        })

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

                        val inputManager =
                            this@MainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.hideSoftInputFromWindow(
                            this@MainActivity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS
                        )

                    } else if (fragmentTitle.equals("Search")) {

                        val f = supportFragmentManager.findFragmentByTag("Search")
                        (f as SearchFragment).onBackPressed()
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
        invite?.setOnClickListener() {
            StaticFunctions.inviteOthers(this)
        }
        globalInvite?.setOnClickListener() {
            StaticFunctions.inviteOthers(this)
        }
        headerBack?.setOnClickListener() {
            onBackPressed()
        }
    }

    fun botnav() {

        val fragment = HomeFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.run {
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

    fun logOut() {
        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", "")
        editor.apply()
        val Preferences = getSharedPreferences("current user", Context.MODE_PRIVATE)
        val mEditor = Preferences.edit()
        mEditor.putString("uname", "noname")
        mEditor.apply()

        Toasty.info(this, "Logout SuccessFully!", Toast.LENGTH_SHORT, true).show()

        ActivityNavigator<LoginActivity>(this@MainActivity, LoginActivity::class.java)

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

    override fun onBackPressed() {

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)


        }else if (fragmentTitle.equals("Search")) {

            val f = supportFragmentManager.findFragmentByTag("Search")
            (f as SearchFragment).onBackPressed()
        }
        else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (isBackEnabled) {
                supportFragmentManager.popBackStack()
            } else {

                // botomNavigation.setCurrentItem(0, true)
                bottomNavigationView.menu.findItem(R.id.bottom_home).setChecked(true)
                bottomNavigationView.selectedItemId = R.id.bottom_home

            }
        } else {
//            super.onBackPressed()

            exitingDailogbox()

        }
    }

    private fun exitingDailogbox() {

        val builder = AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
        builder.setTitle("Exiting..")
        builder.setMessage("Do you realy want to exit?")
        builder.setIcon(R.drawable.tologo)
        builder.setPositiveButton("Exit") { dialog, which ->
            finish()
        }
        builder.setNegativeButton("Cancel", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }


}

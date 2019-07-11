//package com.brainplow.ogrespace.unusedfiles
//
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.graphics.Rect
//import android.os.Bundle
//import android.view.Gravity
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.inputmethod.InputMethodManager
//
//import com.brainplow.ogrespace.R
//import androidx.drawerlayout.widget.DrawerLayout
//import com.google.android.material.navigation.NavigationView
//import androidx.appcompat.app.ActionBarDrawerToggle
//import androidx.appcompat.widget.Toolbar
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.app_bar.*
//
//
//class TestFragment : Fragment() {
//
//    var navigationView: NavigationView? = null
//    var drawerLayout: DrawerLayout? = null
//    var actionBarDrawerToggle: ActionBarDrawerToggle? = null
//    var toolbar: Toolbar? = null
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val view = null
//        navigationView = view.findViewById(R.id.nav_view)
//        drawerLayout = view.findViewById(R.id.drawer_layout)
//        actionBarDrawerToggle =
//            ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
//        //     ViewCompat.setLayoutDirection(toolbar, ViewCompat.LAYOUT_DIRECTION_RTL);
//       // drawerLayout?.setDrawerListener(actionBarDrawerToggle)
//       // navigationView?.setNavigationItemSelectedListener(this)
//        //   actionBarDrawerToggle.setDrawerIndicatorEnabled(false); //disable "hamburger to arrow" drawable
//        //  actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.hamburger); //set your own
//        //actionBarDrawerToggle?.syncState()
////        activity?.toolbar?.setNavigationOnClickListener(object : View.OnClickListener {
////            @SuppressLint("WrongConstant")
////            override fun onClick(v: View) {
////                    drawerLayout?.openDrawer(Gravity.RIGHT)
////
////
////
////            }
////
////        })
//        return view
//    }
//
//
//}

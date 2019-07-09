package com.brainplow.ogrespace.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.activities.MainActivity
import com.brainplow.ogrespace.interfaces.Communicator


class OgreasService : Fragment() {

    var establish_con: TextView? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mcontext=context
        acBarListener=context as Communicator.IActionBar
    }
    var acBarListener: Communicator.IActionBar? = null
    var mcontext: Context? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_ogreas, container, false)

        establish_con = view.findViewById(R.id.establish_con)

        establish_con?.setOnClickListener(View.OnClickListener {
            //            var fragment = Fragment_Feedback()
//            var fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
//            fragmentTransaction?.replace(R.id.content_frame, fragment)
//            fragmentTransaction?.addToBackStack(null)
//            fragmentTransaction?.commit()
        })


        var bSendMail = view.findViewById<View>(R.id.b_help_mail) as TextView
        //
//        bSendMail.setOnClickListener {
//            var intent = Intent(context, EmailUs::class.java)
//            startActivity(intent)
//        }

        return view
    }

    override fun onResume() {
        super.onResume()
        acBarListener?.actionBarListener("Ogre as a Service")
        acBarListener?.isBackButtonEnabled(true)
    }
}// Required empty public constructor

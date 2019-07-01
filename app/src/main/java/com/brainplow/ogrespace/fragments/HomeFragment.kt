package com.brainplow.ogrespace.fragments


import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.LinearLayout
import androidx.drawerlayout.widget.DrawerLayout

import com.brainplow.ogrespace.R
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.DefaultSliderView
import java.util.HashMap

class HomeFragment : Fragment() {
    var main_search_edit: EditText? = null
    var rootView: LinearLayout? = null
    lateinit var mDemoSlider: SliderLayout;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        setIds(view)
        cursorVisible()
        homeSlider()
        return view
    }

    fun cursorVisible(){
        rootView!!.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                val r = Rect()
                rootView!!.getWindowVisibleDisplayFrame(r)
                val screenHeight = rootView!!.getRootView().getHeight()


                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                val keypadHeight = screenHeight - r.bottom;

                //            Log.d("KeyBoard", "keypadHeight = " + keypadHeight)

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    main_search_edit?.isCursorVisible = true


                } else {

                    // if(main_search_edit!=null)
                    main_search_edit?.isCursorVisible = false

                }
            }

        })

    }
    fun setIds(view:View){
        rootView = view.findViewById(R.id.homeFragment)
        main_search_edit = activity?.findViewById(R.id.mainsearch_edittext)
        mDemoSlider = view.findViewById(R.id.banner1);
        mDemoSlider.getPagerIndicator().setDefaultIndicatorColor(getResources().getColor(R.color.Red), getResources().getColor(R.color.gray));

    }

    private fun homeSlider() {


        val url_maps = HashMap<String, Int>()
        url_maps["Electronics"] = R.drawable.slider1
        url_maps["Beats Audio"] = R.drawable.slider2
        url_maps["Apple Mackbook Pro"] =R.drawable.slider3
        url_maps["Home Appliances"] = R.drawable.slider4
        url_maps["Sports"] = R.drawable.slider5

        for (name in url_maps.keys) {
            val textSliderView = DefaultSliderView(activity)
            // initialize a SliderLayout
            textSliderView.image(url_maps[name]!!)
                .setScaleType(BaseSliderView.ScaleType.Fit)

            mDemoSlider.run {
                setBackgroundColor(Color.TRANSPARENT)
                setPresetTransformer(SliderLayout.Transformer.Default)
                setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
                setCustomAnimation(DescriptionAnimation())
                setDuration(5000)
                textSliderView.bundle(Bundle())
                textSliderView.bundle
                    .putString("extra", name)
                addSlider(textSliderView)
            }

            //add your extra information


        }
    }
}

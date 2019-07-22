package com.brainplow.ogrespace.adapters

import android.view.View
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat.getSystemService
import com.brainplow.ogrespace.R
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import androidx.viewpager.widget.ViewPager
import com.brainplow.ogrespace.java.TouchImageView

class FullScreenImageAdapter():PagerAdapter() {

    private var context: Context? = null
    private var imagePaths: ArrayList<String>? = null
    private var inflater: LayoutInflater? = null

    // constructor
   constructor(context:Context?,imagePaths:ArrayList<String>):this(){
        this.context=context
        this.imagePaths=imagePaths
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }

    override fun getCount(): Int {
        return this.imagePaths!!.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var imgDisplay: TouchImageView?=null
        inflater = container.context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewLayout = inflater!!.inflate(R.layout.layout_fullscreen_image, container, false)
        imgDisplay =  viewLayout.findViewById(R.id.imgDisplay)
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap = BitmapFactory.decodeFile(imagePaths!!.get(position), options)
        imgDisplay.setImageBitmap(bitmap)
        (container as ViewPager).addView(viewLayout)

        return viewLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as RelativeLayout)
    }
}
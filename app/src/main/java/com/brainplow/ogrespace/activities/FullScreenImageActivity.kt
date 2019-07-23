package com.brainplow.ogrespace.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.adapters.FullScreenImageAdapter
import com.brainplow.ogrespace.constants.StaticFunctions
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout


class FullScreenImageActivity : AppCompatActivity() {

    var imageMap=HashMap<String,String>()
    var imageList=ArrayList<String>()

    private var adapter: FullScreenImageAdapter? = null
    private var viewPager: ViewPager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_full_screen_image)

        val imageIntent = intent
        imageList=imageIntent.getSerializableExtra("map") as ArrayList<String>
        val pos=imageIntent.getIntExtra("pos",0)
        viewPager = findViewById(R.id.image_pager)
//        for(a in imageMap.keys){
//            imageList.add(imageMap.get(a)!!)
//        }
//        val photoView = findViewById(R.id.photo_view) as PhotoView
//        Glide.with(this)
//            .load(imageList.get(0)).placeholder(R.drawable.placeholder)
//            .into(photoView)
        adapter= FullScreenImageAdapter(this,imageList)
        viewPager!!.setAdapter(adapter)
 //       viewPager!!.setAdapter(SamplePagerAdapter())
//
//        // displaying selected image first
        viewPager!!.setCurrentItem(pos)


    }

    inner class SamplePagerAdapter() : PagerAdapter() {

        override fun getCount(): Int {
            return imageList.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): View {
            val photoView = PhotoView(container.context)
           // photoView.setImageResource(imageList[position])
            // Now just add PhotoView to ViewPager and return it
                    Glide.with(this@FullScreenImageActivity)
            .load(imageList.get(position)).placeholder(R.drawable.placeholder)
            .into(photoView)
            container.addView(photoView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            return photoView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }



    }
}

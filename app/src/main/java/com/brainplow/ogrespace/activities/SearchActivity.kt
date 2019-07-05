package com.brainplow.ogrespace.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.baseclasses.BaseActivity
import com.brainplow.ogrespace.fragments.SearchFragment

class SearchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


    }

    override fun onResume() {
        super.onResume()
        navigateToFragment(SearchFragment())
    }
}

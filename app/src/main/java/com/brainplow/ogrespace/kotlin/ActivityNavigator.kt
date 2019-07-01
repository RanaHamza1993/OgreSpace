package com.brainplow.ogrespace.kotlin

import android.content.Context
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class ActivityNavigator<T>{
   constructor(from: Context,to:Class<T>){
        val intent= Intent(from,to)
        from.startActivity(intent)

    }
    constructor(from: Context, to: Class<T>, extra: Int) {
        val intent = Intent(from, to)
        intent.putExtra("extra", extra)
        from.startActivity(intent)
    }
}
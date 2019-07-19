package com.brainplow.ogrespace.constants

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.brainplow.ogrespace.R
import com.bumptech.glide.Glide
import com.mikhaellopez.circularimageview.CircularImageView
import java.util.regex.Matcher
import java.util.regex.Pattern



object StaticFunctions {
    fun inviteOthers(context: Context?) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Cramfrenzy Invitation")
        sendIntent.putExtra(
            Intent.EXTRA_TEXT, "Hi, I want to invite you to join me to give Cramfrenzy, A Super-Cool Bidding App.\n\n" +
                    "https://play.google.com/store/apps/details?id=com.brainplow.notesgiene&hl=en"
        )
        sendIntent.type = "text/plain"
        context?.startActivity(sendIntent)
    }

    fun hideKeyboard(view: View?, context: Context?) {

        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.getWindowToken(), 0)
    }

    fun emailValidator(email: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(email)
        return matcher.matches()
    }
    fun passwordValidator(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\$&+,:;=?@#|'<>.^*()%!-])(?=\\S+$).{8,}$"
        //  val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\$&+,:;=?@#|'<>.-^*()%!])(?=\\S+$).{8,}$"
        // val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\S+$).{8,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }
    fun loadImage(context:Context?,image:String?,imageView: ImageView?,storage:String){
        Glide.with(context!!)
            .load(storage+image).placeholder(R.drawable.cplaceholder)
            .into(imageView!!)
    }
    fun loadImage(context:Context?,image:String?,imageView: CircularImageView?){
        Glide.with(context!!)
            .load(image).placeholder(R.drawable.cplaceholder)
            .into(imageView!!)
    }
    fun loadImage(context:Context?,image:String?,imageView: CircularImageView?,storage:String){
        Glide.with(context!!)
            .load(storage+image).placeholder(R.drawable.cplaceholder)
            .into(imageView!!)
    }
    fun checkNumberValidation(number:String):Boolean{
        //val regex = "^\\+(?:[0-9] ?){6,14}[0-9]$"
        val regex = "^((\\+92)|(0092))-{0,1}\\d{3}-{0,1}\\d{7}\$|^\\d{11}\$|^\\d{4}-\\d{7}\$\n"
        val  pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(number).matches()
        return matcher
    }

}
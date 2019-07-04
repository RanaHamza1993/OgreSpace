package com.brainplow.ogrespace.fragments


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.error.VolleyError
import com.android.volley.request.SimpleMultiPartRequest
import com.android.volley.toolbox.Volley

import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.baseclasses.BaseFragment
import com.brainplow.ogrespace.constants.StaticFunctions
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.extesnions.showErrorMessage
import com.brainplow.ogrespace.extesnions.showSuccessMessage
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.LoadingDialog
import com.brainplow.ogrespace.kotlin.MySingleton
import com.brainplow.ogrespace.kotlin.VolleyService
import com.mikhaellopez.circularimageview.CircularImageView
import es.dmoral.toasty.Toasty
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.Exception
import java.util.HashMap
import kotlin.math.ln

class ProfileFragment : BaseFragment(),Communicator.IVolleResult {

    override fun notifySuccess(requestType: RequestType?, response: JSONObject?, url: String, netWorkResponse: Int?) {
        if(url.contains(Urls.urlUpdateUserProfile,true)){
            if(filePath!=null)
            imageUpload(filePath)
            mcontext?.showSuccessMessage("Profile updated successfully")
        }

    }

    override fun notifySuccess(requestType: RequestType?, response: String?, url: String, netWorkResponse: Int?) {
        if(url==Urls.urlGetUserProfile){
           val obj=JSONArray(response).getJSONObject(0)
            setUserData(obj)

        }
    }

    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {
       // if(url==Urls.urlGetUserProfile){
            showErrorBody(error)
       // }else iff(url.contains(Urls.urlUpdateUserProfile,true))
    }
    var filePath: String? = null
    var profilepicname: String? = null
    private val PICK_IMAGE_REQUEST: Int = 123
    var userProfile:CircularImageView?=null
    var userFirstName:EditText?=null
    var userLastName:EditText?=null
    var userCountry:EditText?=null
    var userPhoneNo:EditText?=null
    var userState:EditText?=null
    var userCity:EditText?=null
    var userAddress:EditText?=null
    var editProfile:TextView?=null
    var updateProfile: Button?=null
    var volleyService: VolleyService? = null
    var token: String? = null
    var mcontext: Context? = null
    var profileID: Int? = null
    var userProfileName=""
    lateinit var load: LoadingDialog
    var acBarListener: Communicator.IActionBar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mcontext = context
        acBarListener = context as Communicator.IActionBar
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)
        setIds(view)
        setListeners()
        volleyRequests()
        return  view
    }

    override fun onResume() {
        super.onResume()
        acBarListener?.actionBarListener("Profile")
        acBarListener?.isBackButtonEnabled(true)
    }
    fun setIds(view:View){
        val sharedPreferences = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
        token = sharedPreferences?.getString("token", "")
        volleyService = VolleyService(this, mcontext!!.applicationContext)
        load = LoadingDialog("Loading", context)
        userProfile=view.findViewById(R.id.user_profile)
        updateProfile=view.findViewById(R.id.editpf_save)
        userFirstName=view.findViewById(R.id.user__fname)
        userLastName=view.findViewById(R.id.user_lname)
        userCountry=view.findViewById(R.id.user_country)
        userPhoneNo=view.findViewById(R.id.user_mobileno)
        userState=view.findViewById(R.id.user_state)
        userCity=view.findViewById(R.id.user_city)
        userAddress=view.findViewById(R.id.user_address)
        editProfile=view.findViewById(R.id.upload_profile)
    }
    fun setListeners(){
        userProfile?.setOnClickListener {
            selectPicture()
        }
        editProfile?.setOnClickListener {
            selectPicture()
        }
        updateProfile?.setOnClickListener {
            updateUserData()
        }
    }
    fun setUserData(obj:JSONObject){
        try {
            userFirstName?.setText(obj.getString("Fname"))
            userLastName?.setText(obj.getString("Lname"))
            userPhoneNo?.setText(obj.getString("Mobile"))
            userCountry?.setText(obj.getString("Country"))
            userState?.setText(obj.getString("State"))
            userAddress?.setText(obj.getString("Address"))
            userCity?.setText(obj.getString("City"))
            userProfileName=obj.getString("Pic")
            StaticFunctions.loadImage(context,userProfileName,userProfile,Urls.iconStorageUrl)
            profileID=obj.getInt("id")
        }catch (e:Exception){}
    }
    fun volleyRequests(){
        volleyService?.getDataVolley(RequestType.StringRequest, Urls.urlGetUserProfile, token!!)
    }
    fun updateUserData(){
        val isValid=checkValidation()
        if(isValid) {
            val obj = JSONObject()
            obj.put("Fname", userFirstName?.text?.toString())
            obj.put("Lname", userLastName?.text?.toString())
            obj.put("Mobile", userPhoneNo?.text?.toString())
            obj.put("Country", userCountry?.text?.toString())
            obj.put("State", userState?.text?.toString())
            obj.put("Address", userAddress?.text?.toString())
            obj.put("City", userCity?.text?.toString())
            if (filePath != null)
                obj.put("Pic", profilepicname)
            else
                obj.put("Pic", userProfileName)
            volleyService?.putDataVolley(
                RequestType.JsonObjectRequest,
                Urls.urlUpdateUserProfile + profileID + "/",
                obj,
                token!!
            )
        }
    }

    fun  checkValidation():Boolean{
        val ufname=userFirstName?.text?.toString()
        val ulname=userLastName?.text?.toString()
        val unum=userPhoneNo?.text?.toString()
        val ucountry=userCountry?.text?.toString()
        val ustate=userState?.text?.toString()
        val uaddress=userAddress?.text?.toString()
        val ucity=userCity?.text?.toString()
        if(ufname.equals("")){
            mcontext?.showErrorMessage("Enter first name")
            return false
        }
        if(ulname.equals("")){
            mcontext?.showErrorMessage("Enter last name")
            return false
        }
        if(unum.equals("")){
            mcontext?.showErrorMessage("Enter phone number")
            return false
        }
        if(ucountry.equals("")){
            mcontext?.showErrorMessage("Enter country")
            return false
        }
        if(ustate.equals("")){
            mcontext?.showErrorMessage("Enter state")
            return false
        }
        if(ucity.equals("")){
            mcontext?.showErrorMessage("Enter city")
            return false
        }
        if(uaddress.equals("")){
            mcontext?.showErrorMessage("Enter address")
            return false
        }else
            return true
    }
    private fun selectPicture(){
        if (ContextCompat.checkSelfPermission(activity as FragmentActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity as FragmentActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1)
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity as FragmentActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity as FragmentActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            imageBrowse()
        }

    }
    private fun imageBrowse() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.setType("image/*");

        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    imageBrowse()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    ActivityCompat.requestPermissions(activity as FragmentActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        1)
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.

                ActivityCompat.requestPermissions(activity as FragmentActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            val picUri = data?.data
            val selectedImage = data?.data

            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context?.contentResolver?.query(selectedImage!!, filePathColumn, null, null, null)
                ?: return

            cursor.moveToFirst()

            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            filePath = cursor.getString(columnIndex)
            profilepicname = filePath?.substring(filePath!!.lastIndexOf("/") + 1);

            cursor.close()
            var imageStream: InputStream? = null
            try {
                imageStream = context?.contentResolver?.openInputStream(picUri)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            val selectedImage1 = BitmapFactory.decodeStream(imageStream)
            val filePath1 = selectedImage1
            userProfile?.setImageBitmap(filePath1)

        }
    }
    private fun imageUpload(imagePath: String?) {

//        if (!load.ishowingg())
//            load.showdialog()
        val request = object : SimpleMultiPartRequest(
            Request.Method.PUT, Urls.urlImageUpload,
            Response.Listener { s ->

                val imageName = s.toString()
                val a=5
//                    Toast.makeText(mcontext!!, "https://storage.cramfrenzy.com/upload_image.php" + s.toString(), Toast.LENGTH_LONG).show()

               // Toasty.success(mcontext!!,"Image Upload Successfully", Toast.LENGTH_SHORT,true).show()
                // profilepicname=s.toString()
//                if (load.ishowingg())
//                    load.dismisss()
            },
            Response.ErrorListener {

                    volleyError ->
                if (load.ishowingg())
                    load.dismisss()
                // Toast.makeText(context, volleyError.toString(), Toast.LENGTH_LONG).show()
            }) {
            override fun getFilesToUpload(): Map<String, String> {

                val map: HashMap<String, String> = HashMap<String, String>()
                map["fileToUpload"] = imagePath!!
                //map["path"] = "noman/arlsan"
                return map
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json; charset=utf-8"
                return headers
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }

        request.setRetryPolicy(
            DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        )
        MySingleton.getInstance(mcontext!!).requestQueue.add(request)
    }
}

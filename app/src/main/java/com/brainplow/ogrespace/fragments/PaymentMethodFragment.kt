package com.brainplow.ogrespace.fragments


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.error.VolleyError
import com.android.volley.request.JsonObjectRequest
import com.android.volley.request.StringRequest

import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.adapters.CreditCardAdapter
import com.brainplow.ogrespace.apputils.Urls
import com.brainplow.ogrespace.apputils.Urls.urlAddCreditCard
import com.brainplow.ogrespace.apputils.Urls.urlDeleteCreditCard
import com.brainplow.ogrespace.apputils.Urls.urlFromZipCode
import com.brainplow.ogrespace.apputils.Urls.urlGetCreditCard
import com.brainplow.ogrespace.baseclasses.BaseFragment
import com.brainplow.ogrespace.constants.StaticFunctions.hideKeyboard
import com.brainplow.ogrespace.customviews.PaymentCreditEditText
import com.brainplow.ogrespace.enums.RequestType
import com.brainplow.ogrespace.extesnions.showErrorMessage
import com.brainplow.ogrespace.extesnions.showInfoMessage
import com.brainplow.ogrespace.extesnions.showSuccessMessage
import com.brainplow.ogrespace.interfaces.Communicator
import com.brainplow.ogrespace.kotlin.*
import com.brainplow.ogrespace.models.CreditCardModel
import com.google.android.material.navigation.NavigationView
import com.suke.widget.SwitchButton
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_payment_method.*
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume


class PaymentMethodFragment() : BaseFragment(), Communicator.ICreditCard, CoroutineScope,Communicator.IVolleResult {


    override fun notifySuccess(requestType: RequestType?, response: JSONObject?, url: String, netWorkResponse: Int?) {
        if(url.equals(urlAddCreditCard)){
            if(load!!.ishowingg())
                load?.dismisss()
            clearFields()
            context?.showSuccessMessage("Credit card added successfully")
        }
    }

    override fun notifySuccess(requestType: RequestType?, response: String?, url: String, netWorkResponse: Int?) {
        if(url.contains(urlDeleteCreditCard)){
            context?.showInfoMessage("Card deleted successfully")
        }
    }
    override fun notifyError(requestType: RequestType?, error: VolleyError?, url: String, netWorkResponse: Int?) {
        if(url.equals(urlAddCreditCard)){
            if(load!!.ishowingg())
                load?.dismisss()
            showErrorBody(error)
        }
        if(url.contains(urlDeleteCreditCard)){
            context?.showInfoMessage("Card could not be deleted")
        }
    }



    protected lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    var payment_card: LinearLayout? = null
    var volleyService: VolleyService? = null
    var btn_discover: Button? = null
    var btn_master: Button? = null
    var btn_visa: Button? = null
    var add_card: LinearLayout? = null
    var add_card_text: TextView? = null
    var card_detail: LinearLayout? = null
    var card_detail_text: TextView? = null
    var name: EditText? = null
    var rootLayout: LinearLayout?=null
    var card_number: PaymentCreditEditText? = null
    var ccv_number: EditText? = null
    var expiry_date: EditText? = null
    var paymentZipCode: EditText? = null
    var paymentCity: EditText? = null
    var paymentState: EditText? = null
    var paymentCountry: EditText? = null
    var paymentStreetAddress: EditText? = null
    var paymentNickName: EditText? = null
    var add_btn: Button? = null
    var switch_default: SwitchButton? = null
    var payment_Adapter: CreditCardAdapter? = null
    var table_recycler: RecyclerView? = null
    var acBarListener: Communicator.IActionBar? = null
    var mcontext: Context? = null
    var load: LoadingDialog? = null
    var decode: String? = null

    var cardType: String = "Mastercard"
    var CreditCardsList = arrayListOf<CreditCardModel>()
    var datePart1 = ""
    var datePart2 = ""
    var oldLength = 0
    var newLength = 0
    var counter = 1;
    companion object{
        var userid: String? = null
        var token: String? = null
    }
    var checkimg: IntArray = intArrayOf(R.drawable.cross_payment, R.drawable.cross_payment, R.drawable.cross_payment,
        R.drawable.cross_payment, R.drawable.tik_payment)
    lateinit var navigationView: NavigationView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = layoutInflater.inflate(R.layout.fragment_payment_method, container, false)
        navigationView = activity!!.findViewById(R.id.nav_view)
        job = Job()
        setIds(view)
        setListeners()
        return view

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        acBarListener = context as Communicator.IActionBar
        mcontext = context
    }

    fun setIds(view:View){
        load = LoadingDialog("", mcontext)
        val sharedPreferences = activity?.getSharedPreferences("login", Context.MODE_PRIVATE)
        volleyService = VolleyService(this, mcontext!!.applicationContext)
        val date = SimpleDateFormat("MM/yy", Locale.getDefault()).format(Date())
        val parts = date.split("/");
        datePart1 = parts[0]; // 004
        datePart2 = parts[1]; // 034556
        //     println("currentDate"+ datePart1 + "" + datePart2)
        token = sharedPreferences?.getString("token", "")//"No name defined" is the default value.
        acBarListener?.actionBarListener("Payment Methods")
        acBarListener?.isBackButtonEnabled(true)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        table_recycler = view.findViewById(R.id.table_payment_method)
        payment_card = view.findViewById(R.id.payment_card)
        rootLayout=view.findViewById(R.id.parent_linear)
        btn_discover = view.findViewById(R.id.btn_discover)
        btn_master = view.findViewById(R.id.btn_master)
        btn_visa = view.findViewById(R.id.btn_visa)
        add_card_text = view.findViewById(R.id.add_card_txt)
        //  card_detail=view.findViewById(R.id.card_detail)
        card_detail_text = view.findViewById(R.id.card_detail_txt)
        name = view.findViewById(R.id.name_edit_text)
        card_number = view.findViewById(R.id.card_no)
        ccv_number = view.findViewById(R.id.cvv_no)
        expiry_date = view.findViewById(R.id.expiry_date)
        paymentZipCode = view.findViewById(R.id.payment_zipcode)
        paymentCity = view.findViewById(R.id.payment_city)
        paymentCountry = view.findViewById(R.id.payment_country)
        paymentState = view.findViewById(R.id.payment_state)
        paymentStreetAddress = view.findViewById(R.id.payment_street)
        paymentNickName = view.findViewById(R.id.payment_nick_name)
        add_btn = view.findViewById(R.id.add_btn)
        switch_default = view.findViewById(R.id.switch_button)

    }
    override fun onResume() {
        super.onResume()

        if (counter == 1) {
            add_card_text?.performClick()
        } else {
            card_detail_text?.performClick()
        }
        acBarListener?.isBackButtonEnabled(true)
        acBarListener?.toolbarBackground(false)
    }

    private fun AddCreditCard() {
        val isValidated = checkValidation()
        if (isValidated) {
            val rootObject = JSONObject()
            // rootObject.put("initial_amount", profilepicname.toString())
            val cardnumber = card_number?.text?.toString()
            val cardname = name?.text?.toString()
            var expirydate = expiry_date?.text?.toString()
            expirydate = expirydate?.replace("/", "")
            val cvvnumber = ccv_number?.text?.toString()
            val isChecked = switch_default?.isChecked
            rootObject.put("number", cardnumber?.replace("-", ""))
            rootObject.put("name", cardname)
            rootObject.put("expDate", expirydate)
            rootObject.put("cvc", cvvnumber)
            rootObject.put("card_type", cardType)
            rootObject.put("nickname", paymentNickName?.text?.toString())
            rootObject.put("state", paymentState?.text?.toString())
            rootObject.put("city", paymentCity?.text?.toString())
            rootObject.put("country", paymentCountry?.text?.toString())
            rootObject.put("zipcode", paymentZipCode?.text?.toString()?.toInt())
            rootObject.put("street_adrress", paymentStreetAddress?.text?.toString())
            rootObject.put("autopay", isChecked)
          //  rootObject.put("pinCode", 143)
            load?.showdialog()
            volleyService?.postDataVolley(
                RequestType.JsonObjectRequest,
                urlAddCreditCard,
                rootObject,
                token!!
            )

        }
    }


//    private suspend fun getCreditCards()= suspendCoroutine<String> {
//
//
//        val jsonObjectRequest =
//        jsonObjectRequest.setShouldCache(false)
//        MySingleton.getInstance(mcontext!!.applicationContext).addToRequestQueue(jsonObjectRequest)
//    }

    suspend fun getCreditCards(): String = suspendCancellableCoroutine { continuation ->
        CreditCardsList.clear()
        getStringData(mcontext,urlGetCreditCard, token,continuation).getStringRequest()
    }
    private fun getFromZipCode(zipCode: Int) {
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, urlFromZipCode + zipCode, null,
            Response.Listener { response ->
                paymentCountry?.setText(response?.getString("country"))
                paymentCity?.setText(response?.getString("city"))
                paymentState?.setText(response?.getString("state"))

                try {
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            },
            Response.ErrorListener { error ->
                // showErrorBody(error)
                val statusCode = error.networkResponse.statusCode
                if (statusCode == 400)
                    context?.showErrorMessage("Invalid zipcode")
            }
        ) {


            override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                return super.parseNetworkResponse(response)
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json; charset=utf-8"
                if (!token.equals(""))
                    headers.put("Authorization", "JWT " + token)
                return headers
            }
        }
        MySingleton.getInstance(mcontext!!.applicationContext).addToRequestQueue(jsonObjectRequest)
    }

    override fun updateCreditCard(id: Int) {
//        val args = Bundle()
//        args.putInt("id", id)
//        var fragment = UpdateCreditCardFragment()
//        fragment.arguments = args
//        var fragmentTransaction = fragmentManager?.beginTransaction()
//        fragmentTransaction?.replace(R.id.content_frame, fragment)
//        fragmentTransaction?.addToBackStack(null)
//        counter++;
//        fragmentTransaction?.commit()

    }

    override fun deleteCreditCard(id: Int) {
        volleyService?.deleteDataVolley(
            RequestType.StringRequest,
            urlDeleteCreditCard+id,
            token!!
        )

    }

    fun checkValidation(): Boolean {
        val cardnumber = card_number?.text?.toString()?.trim()
        val cardname = name?.text?.toString()?.trim()
        var expirydate = expiry_date?.text?.toString()?.trim()
        expirydate = expirydate?.replace("/", "")
        val cvvnumber = ccv_number?.text?.toString()
        val zipCode = paymentZipCode?.text?.toString()
        val city = paymentCity?.text?.toString()
        val country = paymentCountry?.text?.toString()
        val state = paymentState?.text?.toString()
        val nickName = paymentNickName?.text?.toString()
        val streedAddress = paymentStreetAddress?.text?.toString()
        if (cardnumber!!.isEmpty()) {

            //card_number?.error = "Enter Card Number"
            context?.showErrorMessage("Enter card number")
            return false
        } else if (cardnumber.length < 19) {
            //card_number?.error = "Enter Full Card Number"
            context?.showErrorMessage("Enter full card number")
            return false
        } else if(cardType.equals("Mastercard",true)&& !cardnumber.replace("-","").matches("^5[1-5][0-9]{1,14}$".toRegex())){

            context?.showErrorMessage("Invalid master cardnumber")
            return false
        }else if(cardType.equals("Visa",true)&& !cardnumber.replace("-","").matches("^4[0-9]{2,12}(?:[0-9]{3})?$".toRegex())){

            context?.showErrorMessage("Invalid visa cardnumber")
            return false
        }else if(cardType.equals("Discover",true)&& !cardnumber.replace("-","").matches("^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{10})$".toRegex())){

            context?.showErrorMessage("Invalid discover cardnumber")
            return false
        }
        else if (cvvnumber!!.isEmpty()) {

            // ccv_number?.error = "Enter Cvv Number"
            context?.showErrorMessage("Enter cvv number")
            return false
        } else if (cvvnumber.length < 3) {

            //ccv_number?.error = "Enter Full Cvv Number"
            context?.showErrorMessage("Enter full cvv number")
            return false
        } else if (expirydate!!.isEmpty()) {

            //expiry_date?.error = "Enter Expiry Date"
            context?.showErrorMessage("Enter expiry date")
            return false
        } else if (expirydate.length < 4) {
            //  expiry_date?.error = "Invalid Expiry Date"
            context?.showErrorMessage("Invalid expiry date")
            return false
        } else if (zipCode!!.length < 5) {
            //   expiry_date?.error = "Invalid ZipCode"
            context?.showErrorMessage("Invalid zipcode")
            return false
        } else if (city!!.isEmpty()) {
            context?.showErrorMessage("Invalid zipcode")
            return false
        } else if (streedAddress!!.isEmpty()) {
            //expiry_date?.error = "Enter Street Address"
            context?.showErrorMessage("Enter street address")
            return false
        } else if (cardname!!.isEmpty()) {

            //name?.error = "Enter Name"
            context?.showErrorMessage("Enter Cardholder Name")
            return false
        } else if (cardname.length<2) {
            context?.showErrorMessage(" Cardholder Name must be between 2 and 14 characters")
            //name?.error = "Enter Name"
            return false
        } else if (nickName!!.isEmpty()) {
            //expiry_date?.error = "Enter Street Address"
            context?.showErrorMessage("Enter nickname")
            return false
        } else if (nickName.length < 2) {
            //expiry_date?.error = "Enter Street Address"
            context?.showErrorMessage(" Nick Name name must be between 2 and 14 characters")
            return false
        }  else {
            val mon = expirydate?.substring(0, 2).toInt()
            val year = expirydate?.substring(2).toInt()
            if (mon < 1 || mon > 12) {
                expiry_date?.error = "Invalid Expiry Date"
                return false
            } else if (year < datePart2.toInt()) {
                expiry_date?.error = "Invalid Expiry Date"
                return false
            } else if ((year == datePart2.toInt()) && (mon < datePart1.toInt())) {
                expiry_date?.error = "Invalid Expiry Date"
                return false
            }
            return true
        }

    }

    fun setListeners() {
        paymentZipCode?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length == 5)
                    getFromZipCode(s.toString().toInt())
                else {
                    paymentState?.setText("")
                    paymentCountry?.setText("")
                    paymentCity?.setText("")
                }
            }

        })
        card_number?.addTextChangedListener(object : TextWatcher {

            private val TOTAL_SYMBOLS = 19 // size of pattern 0000-0000-0000-0000
            private val TOTAL_DIGITS = 16 // max numbers of digits in pattern: 0000 x 4
            private val DIVIDER_MODULO = 5 // means divider position is every 5th symbol beginning with 1
            private val DIVIDER_POSITION = DIVIDER_MODULO - 1 // means divider position is every 4th symbol beginning with 0
            private val DIVIDER = '-'

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // noop
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // noop
            }

            override fun afterTextChanged(s: Editable) {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length, buildCorrectString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER))
                }
            }

            private fun isInputCorrect(s: Editable, totalSymbols: Int, dividerModulo: Int, divider: Char): Boolean {
                var isCorrect = s.length <= totalSymbols // check size of entered string
                for (i in 0 until s.length) { // check that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect = isCorrect and (divider == s[i])
                    } else {
                        isCorrect = isCorrect and Character.isDigit(s[i])
                    }
                }
                return isCorrect
            }

            private fun buildCorrectString(digits: CharArray, dividerPosition: Int, divider: Char): String {
                val formatted = StringBuilder()

                for (i in digits.indices) {
                    if (digits[i].toInt() != 0) {
                        formatted.append(digits[i])
                        if (i > 0 && i < digits.size - 1 && (i + 1) % dividerPosition == 0) {
                            formatted.append(divider)
                        }
                    }
                }

                return formatted.toString()
            }

            private fun getDigitArray(s: Editable, size: Int): CharArray {
                val digits = CharArray(size)
                var index = 0
                var i = 0
                while (i < s.length && index < size) {
                    val current = s[i]
                    if (Character.isDigit(current)) {
                        digits[index] = current
                        index++
                    }
                    i++
                }
                return digits
            }
        })
        expiry_date?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                //c=s!!.length
                if (oldLength == 1 && newLength == 2) {
                    s!!.append("/")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                oldLength = s!!.length

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                newLength = s!!.length

            }

        })

        expiry_date?.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {


                if (hasFocus) {

                    expiry_helper?.helperText = "e.g: MM/YY"
                } else {
                    expiry_helper?.helperText = ""
                }


            }

        }

        add_btn?.setOnClickListener({
            AddCreditCard()
        })
        btn_master?.background = resources.getDrawable(R.drawable.creditcardbg_black)
        btn_master?.setTextColor(resources.getColor(R.color.white))
        btn_visa?.background = resources.getDrawable(R.drawable.creditcardbg)
        btn_visa?.setTextColor(resources.getColor(R.color.black))
        btn_discover?.background = resources.getDrawable(R.drawable.creditcardbg)
        btn_discover?.setTextColor(resources.getColor(R.color.black))


        btn_discover?.setOnClickListener {
            clearFields()
            cardType = "Discover"
            btn_master?.background = resources.getDrawable(R.drawable.creditcardbg)
            btn_master?.setTextColor(resources.getColor(R.color.black))

            btn_visa?.background = resources.getDrawable(R.drawable.creditcardbg)
            btn_visa?.setTextColor(resources.getColor(R.color.black))
            btn_discover?.background = resources.getDrawable(R.drawable.creditcardbg_black)
            btn_discover?.setTextColor(resources.getColor(R.color.white))
            card_number?.setcardType("discover")
            card_number?.filters= arrayOf(object: InputFilter {
                override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence {
                    if(dest!!.toString().equals("")&&!source!!.equals("6"))
                        return ""
                    else  return source!!
                }

            })

        }
        btn_master?.setOnClickListener {
            clearFields()
            cardType = "Mastercard"
            btn_master?.background = resources.getDrawable(R.drawable.creditcardbg_black)
            btn_master?.setTextColor(resources.getColor(R.color.white))
            btn_visa?.background = resources.getDrawable(R.drawable.creditcardbg)
            btn_visa?.setTextColor(resources.getColor(R.color.black))
            btn_discover?.background = resources.getDrawable(R.drawable.creditcardbg)
            btn_discover?.setTextColor(resources.getColor(R.color.black))
            card_number?.setcardType("master")
            card_number?.filters= arrayOf(object: InputFilter {
                override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence {
                    if(dest!!.toString().equals("")&&!source!!.equals("5"))
                        return ""
                    else  return source!!
                }

            })
        }
        btn_master?.performClick()
        btn_visa?.setOnClickListener {
            clearFields()
            cardType = "Visa"
            btn_master?.background = resources.getDrawable(R.drawable.creditcardbg)
            btn_master?.setTextColor(resources.getColor(R.color.black))

            btn_visa?.background = resources.getDrawable(R.drawable.creditcardbg_black)
            btn_visa?.setTextColor(resources.getColor(R.color.white))
            btn_discover?.background = resources.getDrawable(R.drawable.creditcardbg)
            btn_discover?.setTextColor(resources.getColor(R.color.black))
            card_number?.setcardType("visa")
            card_number?.filters= arrayOf(object: InputFilter {
                override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence {
                    if(dest!!.toString().equals("")&&!source!!.equals("4"))
                        return ""
                    else  return source!!
                }

            })

        }
        //add_card?.setBackgroundColor(resources.getColor(R.color.black))
        //   add_card?.background = resources.getDrawable(R.drawable.bidcarbtnradius)
        // card_detail?.background = resources.getDrawable(R.drawable.bidcarbtnradius_black)
        //card_detail?.setBackgroundColor(Color.WHITE)


        add_card_text?.setOnClickListener({

            add_card_text?.setBackgroundColor(resources.getColor(R.color.black))
            add_card_text?.setTextColor(resources.getColor(R.color.white))
            card_detail_text?.setTextColor(resources.getColor(R.color.black))
            card_detail_text?.setBackgroundColor(resources.getColor(R.color.white))
            payment_card?.visibility = View.VISIBLE
            table_recycler?.visibility = View.GONE
        })

        card_detail_text?.setOnClickListener(){

            card_detail_text?.setBackgroundColor(resources.getColor(R.color.black))
            card_detail_text?.setTextColor(resources.getColor(R.color.white))
            add_card_text?.setTextColor(resources.getColor(R.color.black))
            add_card_text?.setBackgroundColor(resources.getColor(R.color.white))

            payment_card?.visibility = View.GONE
            table_recycler?.visibility = View.VISIBLE
            launch {
                val creditResult: Deferred<String> =   async {  getCreditCards() }
                creditCardParsing(creditResult.await())

            }


        }

//        btn_pay.setOnClickListener{
//            btn_pay.setBackgroundResource(R.drawable.border)
//            btn_pay.setTextColor(Color.BLACK)
//        }


//        expiry_date?.addTextChangedListener(object : TextWatcher {
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                var working = s.toString()
//                var isValid = true
//                if (working.length == 2 && before == 0) {
//                    if (Integer.parseInt(working) < 1 || Integer.parseInt(working) > 12) {
//                        isValid = false
//                    } else {
//                        working += "/"
//                        expiry_date?.setText(working)
//                        expiry_date?.setSelection(working.length)
//                    }
//                } else if (working.length == 5 && before == 0) {
//                    val enteredYear = working.substring(3)
//                    val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100//getting last 2 digits of current year i.e. 2018 % 100 = 18
//                    if (Integer.parseInt(enteredYear) < currentYear) {
//                        isValid = false
//                    }
//                } else if (working.length != 5) {
//                    isValid = false
//                }
//
//                if (!isValid) {
//                    expiry_date?.error = "Enter Valid Expiry Date"
//                } else {
//                    expiry_date?.error = null
//                }
//
//            }
//
//            override fun afterTextChanged(s: Editable) {}
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//        })


        table_recycler?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        setListeners()
        rootLayout!!.setOnClickListener({
            hideKeyboard(it,context)
        })
    }

    fun clearFields() {
        card_number?.setText("")
        name?.setText("")
        expiry_date?.setText("")
        ccv_number?.setText("")
        paymentNickName?.setText("")
        paymentStreetAddress?.setText("")
        paymentState?.setText("")
        paymentCountry?.setText("")
        paymentCity?.setText("")
        paymentZipCode?.setText("")

        switch_default?.isChecked = false

    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }
    fun creditCardParsing(resp:String?){
        val response= JSONArray(resp)
        //showErrorBody()
        try {
            for (i in 0 until response.length()) {

                val jsonObject = response.getJSONObject(i)
                val cid = jsonObject?.getString("id")?.toInt()
                val cardname = jsonObject?.getString("nickname")
                val cardnumber = jsonObject?.getString("cardNumber")
                val expirydate = jsonObject?.getString("expiryDate")
                val defaultvalue = jsonObject?.getBoolean("autopay")
             //   val cardtype = jsonObject?.getString("card_type")

                val creditCard = CreditCardModel(id = cid, cardNumber = cardnumber, nickname = cardname,
                    ccv = null, default = defaultvalue, expiryDate = expirydate,
                    user = null, cardType = null)

                CreditCardsList.add(creditCard)
            }


            val adapter = CreditCardAdapter(mcontext!!, CreditCardsList, 0)
            adapter.setUpdateCardListener(this)
            adapter.setDeleteCardListener(this)
            table_recycler?.adapter = adapter



            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    class RequestWithHeaders(
        continuation: Continuation<String>
    )  : StringRequest(
        Request.Method.GET, urlGetCreditCard + PaymentMethodFragment.userid,
        Response.Listener { response ->
            continuation.resume(response)
        },
        Response.ErrorListener { error ->

            //  it.resume(JSONArray(error.toString()))

        }
    )
    {


        override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
            return super.parseNetworkResponse(response)
        }

        override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            headers["Content-Type"] = "application/text; charset=utf-8"
            if (!token.equals(""))
                headers.put("Authorization", "JWT " + token)
            return headers
        }
    }
}



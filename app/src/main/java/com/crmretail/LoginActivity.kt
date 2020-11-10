package com.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.*
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.crmretail.PostInterface
import com.crmretail.R
import com.crmretail.activity.HomeActivity
import com.crmretail.modelClass.*
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() , View.OnClickListener{


    private lateinit var nextpage :TextView

    lateinit var edt_email : EditText
    lateinit var edt_pass : EditText

    lateinit var EmailStr: String
    lateinit var PasswordStr: String
    lateinit var prefs: SharedPreferences

    lateinit var progressDialog : ProgressDialog

    lateinit var forgotPassword:TextView

    val RC_SIGN_IN: Int = 1


    lateinit var textView27:TextView
    lateinit var textView28:TextView

    var personal_info : ArrayList<PersonalInfo>?=null
    var area_info : ArrayList<AreaInfo>?=null

    var product_info : ArrayList<ProductInfo>?=null

    private var LATSTR=""
    private var LONGSTR=""

    private var googleApiClient: GoogleApiClient? = null
    val REQUEST_CHECK_SETTINGS = 0x1
    var flag: Boolean?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.login_activity)
        prefs = getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE)

        progressDialog =ProgressDialog(this)



        LocalBroadcastManager.getInstance(this).registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    // String latitude = intent.getStringExtra("Latitude");
                    // String longitude = intent.getStringExtra("Longitude");
                    val provider = intent.getStringExtra("GpsStatus")
                    if (provider != null) {
                        Log.v("prooooo", provider)
                        if (provider == "Gps Disabled") {
                            myCheck()
                        }

                        // mMsgView.setText(getString(R.string.msg_location_service_started) + "\n Latitude : " + latitude + "\n Longitude: " + longitude);
                    }
                }
            }, IntentFilter("Hellooooo")
        )



        intv()
        getLocation()



    }

    private fun myCheck() {


        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(this@LoginActivity)
                .addApi(LocationServices.API).build()
            googleApiClient!!.connect()
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 30 * 1000.toLong()
            locationRequest.fastestInterval = 5 * 1000.toLong()
            val builder =
                LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            builder.setAlwaysShow(true)
            builder.setNeedBle(true)
            val result: PendingResult<LocationSettingsResult> =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
            result.setResultCallback(object : ResultCallback<LocationSettingsResult?> {
                override fun onResult(result: LocationSettingsResult) {
                    val status: Status = result.getStatus()
                    val state: LocationSettingsStates = result.getLocationSettingsStates()
                    when (status.statusCode) {
                        LocationSettingsStatusCodes.SUCCESS -> {
                            // Toast.makeText(getApplicationContext(), "GPS Is ON", Toast.LENGTH_SHORT).show()

                        }
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            status.startResolutionForResult(
                                this@LoginActivity,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (e: IntentSender.SendIntentException) {
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        }
                    }
                }
            })
        }
    }


    fun getLocation() {

        var locationManager = getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?

        var locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                var latitute = location!!.latitude
                var longitute = location!!.longitude

                Log.i("test", "Latitute: $latitute ; Longitute: $longitute")
                LATSTR=location!!.latitude.toString()
                LONGSTR=location!!.longitude.toString()

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }

        }

        try {
            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch (ex:SecurityException) {
            Toast.makeText(this, "Fehler bei der Erfassung!", Toast.LENGTH_SHORT).show()
        }
    }





    lateinit var EMAIL:String
    lateinit var USER_POSTS:String
    lateinit var AUTH_TYPE:String

    private fun intv() {

        nextpage=findViewById(R.id.logintxt) as TextView

        edt_email=findViewById(R.id.email_id)
        edt_pass=findViewById(R.id.password)

        forgotPassword=findViewById(R.id.forgot_password)






        nextpage.setOnClickListener(this)
        forgotPassword.setOnClickListener(this)


        requestPermission()


    }

    private fun requestPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.CAMERA



            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                         Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        // ScanFinction();

                        flag=true


                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied()) {
                        // show alert dialog navigating to Settings
                        //Toast.makeText(getApplicationContext(), "All permissions are Denied!", Toast.LENGTH_SHORT).show();
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener(object : PermissionRequestErrorListener {
                override fun onError(error: DexterError) {
                    Toast.makeText(applicationContext, "Error occurred! " + error.toString(), Toast.LENGTH_SHORT).show()
                }
            })
            .onSameThread()
            .check()
    }

    private fun showSettingsDialog() {



        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setTitle("Need Permissions")
        dialogBuilder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        dialogBuilder.setPositiveButton("GOTO SETTINGS", { dialog, whichButton ->


            dialog.dismiss()
            //openSettings()
            requestPermission()
        })
        dialogBuilder.setNegativeButton("Cancel", { dialog, whichButton ->



            dialog.dismiss()
        })
        val b = dialogBuilder.create()
        b.show()
    }


    override fun onClick(v: View?) {


        when(v?.id){


            R.id.logintxt->{

                if (flag==true){

                    validation()
                }
                else{
                    requestPermission()
                }


            }

            R.id.forgot_password->{

              //  checkV()

               // startActivity(Intent(this@LoginActivity, ForgotPassword::class.java))
            }


        }
    }










    private fun validation() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        getLocation()
        EmailStr = edt_email.getText().toString()
        PasswordStr = edt_pass.getText().toString()


        var cancel = false
        var message = ""
        var focusView: View? = null
        var tempCond = false




        if (TextUtils.isEmpty(PasswordStr)) {
            message = "Please enter your password"
            focusView = edt_pass
            cancel = true
            tempCond = false
        }








        if (TextUtils.isEmpty(EmailStr)) {
            message = "Please enter your email address."
            focusView = edt_email
            cancel = true
            tempCond = false
        }

        if (LATSTR==null&& LONGSTR==null||LATSTR.equals("0.0")&&LONGSTR.equals("0.0")){
            message = "Please check your GPS"
            cancel = true
            tempCond = false

        }






        if (cancel) {
            // focusView.requestFocus();
            if (!tempCond) {
                focusView!!.requestFocus()
            }
            showToastLong(message)
        } else {
            val imm = this
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager



            if(!PostInterface.isConnected(this@LoginActivity)){

                Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            }
            else{

                progressDialog.setMessage("Please wait ...")
                progressDialog.setCancelable(false)
                progressDialog.show()
                callSignIN(EmailStr,PasswordStr)
                val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(edt_email.windowToken, 0)

            }


        }




    }




    private fun callSignIN(email: String?,password:String?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.



        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.loginCall(email!!,password!!,"1","1",LATSTR,LONGSTR).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    //val statusCode = response.code()
                    val avv = response.body()!!.id
                    Log.i("onSuccess", avv.toString());
                    if (avv!=null) {

                        Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_SHORT).show()

                        val editor = prefs.edit()
                        editor.putBoolean(getString(R.string.shared_loggedin_status), true)
                        editor.putString(getString(R.string.shared_user_id),response.body()!!.id.toString())
                        editor.putString(getString(R.string.shared_user_full_name),response.body()!!.name)
                        editor.putString(getString(R.string.shared_access_token),response.body()!!.accessToken)

                        val gson = Gson()
                        val areaData: String = gson.toJson(response.body()!!.areaInfo)
                        editor.putString(getString(R.string.areaData),areaData)

                        val gsonp = Gson()
                        val productData: String = gsonp.toJson(response.body()!!.productInfo)
                        editor.putString(getString(R.string.productlist),productData)


                        val gsonper = Gson()
                        val profleData: String = gsonper.toJson(response.body()!!.personalInfo)
                        editor.putString(getString(R.string.personallist),profleData)


                        val gsonpers = Gson()
                        val checklistData: String = gsonpers.toJson(response.body()!!.checkList)
                        editor.putString(getString(R.string.checklist),checklistData)


                      //  val customer_Info_8: String = gson.toJson(response.body()!!.customerInfo.get8())
                       // editor.putString(getString(R.string.customerinfo),customer_Info_8)

                        //val customer_Info_81: String = gson.toJson(response.body()!!.customerInfo.get81())
                        //editor.putString(getString(R.string.customerinfo81),customer_Info_81)

                       /* val gson = GsonBuilder().create()
                        ///personalData
                        val arrayPersonalInfo =response.body()!!.personalInfo as ArrayList<PersonalInfo>
                        personal_info = ArrayList<PersonalInfo>()
                        val modelp= gson.fromJson(arrayPersonalInfo.toString(),Array<PersonalInfo>::class.java).toList()
                        personal_info!!.addAll(modelp)
                        val jsonP =gson.toJson(personal_info)

                        ////areadata
                        val arrayAreaInfo =response.body()!!.areaInfo as ArrayList<AreaInfo>
                        area_info = ArrayList<AreaInfo>()
                        val modelA= gson.fromJson(arrayAreaInfo.toString(),Array<AreaInfo>::class.java).toList()
                        area_info!!.addAll(modelA)
                        val jsonA =gson.toJson(area_info)

                        ///customerinfor

                        val arraycustomerInfo =response.body()!!.customerInfo as ArrayList<CustomerInfo>
                        customer_info = ArrayList<CustomerInfo>()
                        val modelC= gson.fromJson(arraycustomerInfo.toString(),Array<CustomerInfo>::class.java).toList()
                        customer_info!!.addAll(modelC)
                        val jsonC =gson.toJson(customer_info)


                        ///product_info

                        val arrayproductInfo =response.body()!!.productInfo
                        product_info = ArrayList<ProductInfo>()
                        val modelPr= gson.fromJson(arrayproductInfo.toString(),Array<ProductInfo>::class.java).toList()
                        product_info!!.addAll(modelPr)
                        val jsonPr =gson.toJson(product_info)



                        editor.putString(getString(R.string.personallist),jsonP)
                        editor.putString(getString(R.string.arealist),jsonA)
                        editor.putString(getString(R.string.customerlist),jsonC)
                        editor.putString(getString(R.string.productlist),jsonPr)*/


                        editor.commit()

                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))



                    } else {
                        Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Invalid Credentials !!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }


    private fun showToastLong(message: String) {

        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()

    }

    override fun onBackPressed() {

        finishAffinity()
        finish()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
              //  Toast.makeText(applicationContext, "GPS enabled", Toast.LENGTH_LONG).show()

            } else {

                onBackPressed()
            }
        }
    }

}
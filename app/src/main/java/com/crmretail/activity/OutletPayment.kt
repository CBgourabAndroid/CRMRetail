package com.crmretail.activity

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crmretail.PostInterface
import com.crmretail.R
import com.crmretail.adapter.PaymentAdapter
import com.crmretail.modelClass.OutstandingResponce
import com.crmretail.modelClass.Payment
import com.crmretail.modelClass.PaymentResponce
import com.crmretail.shared.UserShared
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OutletPayment : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var progressDialog: ProgressDialog
    lateinit var psh: UserShared
    lateinit var recyclerView:RecyclerView
    lateinit var payAdapter:PaymentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.outlet_payment_history)

        psh=UserShared(this)
        progressDialog= ProgressDialog(this)
        toolbar = findViewById(R.id.toolbar) as Toolbar

        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)



        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }


        recyclerView = findViewById(R.id.report_rcy_view)

        if(!PostInterface.isConnected(this@OutletPayment)){

            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
        else{

            progressDialog.setMessage("Please wait ...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            callDataList()
        }


    }


    private fun callDataList() {



        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.payments("Bearer "+psh.accessToken,intent.getStringExtra("shopid").toString()).enqueue(object :
            Callback<PaymentResponce> {
            override fun onResponse(call: Call<PaymentResponce>, response: Response<PaymentResponce>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {


                      //  Toast.makeText(applicationContext, "Good", Toast.LENGTH_SHORT).show()

                        if (response.body()!!.payments!=null&& response.body()!!.payments.size>0){

                            payAdapter= PaymentAdapter()
                            recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL ,false)
                            recyclerView.adapter = payAdapter
                            payAdapter.setHistoryListItems(this@OutletPayment,
                                response.body()!!.payments as ArrayList<Payment>
                            )
                        }
                        else{

                            Toast.makeText(this@OutletPayment,"No Data Found!!",Toast.LENGTH_SHORT).show()
                        }





                    } else {
                        // Toast.makeText(applicationContext, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Fail!!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PaymentResponce>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }
}
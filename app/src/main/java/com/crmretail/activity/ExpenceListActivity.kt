package com.crmretail.activity

import android.app.ProgressDialog
import android.content.Intent
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
import com.crmretail.adapter.ExpenceListAdapter
import com.crmretail.adapter.LeaveListAdapter
import com.crmretail.modelClass.ExpenceListResponse
import com.crmretail.modelClass.Expense
import com.crmretail.modelClass.LeaveList
import com.crmretail.modelClass.LeaveListResponse
import com.crmretail.shared.UserShared
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.leave_application.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExpenceListActivity : AppCompatActivity() {


    lateinit var progressDialog : ProgressDialog
    lateinit var psh: UserShared
    lateinit var toolbar: Toolbar
    lateinit var recyclerView: RecyclerView

    lateinit var dataList:ArrayList<Expense>
    lateinit var hAdapter: ExpenceListAdapter
    lateinit var addLeave: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.leave_application)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)



        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }

        progressDialog= ProgressDialog(this)
        psh= UserShared(this)
        textView6.setText("Expense List")

        recyclerView = findViewById(R.id.report_rcy_view)
        if(!PostInterface.isConnected(this@ExpenceListActivity)){

            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
        else{

            progressDialog.setMessage("Please wait ...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            callDataList()
        }

        addLeave=findViewById(R.id.fab)
        addLeave.setOnClickListener {

            val intent = Intent(this@ExpenceListActivity, Expenses::class.java)
            startActivityForResult(intent, 123)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==123){

            val message = data!!.getStringExtra("isOk")

            if (!message.equals("")){

                if(!PostInterface.isConnected(applicationContext)){

                    Toast.makeText(applicationContext, applicationContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                }
                else{

                    progressDialog.setMessage("Please wait ...")
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                    callDataList()


                }
            }
            else{
                Toast.makeText(applicationContext, "No Data Added!!", Toast.LENGTH_SHORT).show()
            }


        }
    }

    private fun callDataList() {



        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(PostInterface.BaseURL)
            .build()

        val userAPI = retrofit.create(PostInterface::class.java!!)

        userAPI.getexpenceList("Bearer "+psh.accessToken,psh.id).enqueue(object :
            Callback<ExpenceListResponse> {
            override fun onResponse(call: Call<ExpenceListResponse>, response: Response<ExpenceListResponse>) {
                println("onResponse")
                System.out.println(response.toString())

                if (response.code()==200){
                    progressDialog.dismiss()

                    val statusCode = response.code()
                    val avv = response.body()!!.status
                    Log.i("onSuccess", avv.toString());
                    if (avv==200) {

                        dataList= ArrayList()


                        // Toast.makeText(applicationContext, "Good", Toast.LENGTH_SHORT).show()

                        if (response.body()!!.expenses!=null&& response.body()!!.expenses.size>0){

                            hAdapter= ExpenceListAdapter()
                            recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL ,false)
                            recyclerView.adapter = hAdapter

                            dataList=response.body()!!.expenses as ArrayList<Expense>

                            hAdapter.setHistoryListItems(this@ExpenceListActivity,
                                dataList
                            )
                        }
                        else{

                            Toast.makeText(this@ExpenceListActivity,"No Data Found!!", Toast.LENGTH_SHORT).show()
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

            override fun onFailure(call: Call<ExpenceListResponse>, t: Throwable) {
                println("onFailure")
                println(t.fillInStackTrace())
                progressDialog.dismiss()
            }
        })
    }
}
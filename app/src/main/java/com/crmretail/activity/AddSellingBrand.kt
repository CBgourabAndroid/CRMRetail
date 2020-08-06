package com.crmretail.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.crmretail.R
import com.google.android.material.textfield.TextInputEditText
import java.io.*

class AddSellingBrand : AppCompatActivity() {

    lateinit var backImg: ImageView
    lateinit var saveData: TextView
    lateinit var brandName: TextInputEditText
    lateinit var brandQuantity: TextInputEditText

    var BrandName=""
    var Quantity=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.add_selling_brand)

        backImg=findViewById(R.id.imageView4)
        backImg.setOnClickListener {

            onBackPressed()
        }


        inti()
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("name", "")
        intent.putExtra("qty", "")
        setResult(123, intent)
        finish()
    }

    private fun inti() {


        brandName=findViewById(R.id.foodTypeName)
        brandQuantity=findViewById(R.id.foodTypePrice)
        saveData=findViewById(R.id.savetxt)

        saveData.setOnClickListener {


            validation()
        }

    }



    @SuppressLint("NewApi")
    private fun validation() {


        var cancel = false
        var message = ""
        var focusView: View? = null
        var tempCond = false

        BrandName=brandName.text.toString()
        Quantity=brandQuantity.text.toString()


        if (TextUtils.isEmpty(BrandName)) {
            message = "Please Enter Brand Name"
            focusView = brandName
            cancel = true
            tempCond = false
        }




        if (TextUtils.isEmpty(Quantity)) {
            message = "Please Enter Quantity"
            focusView = brandQuantity
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

            val intent = Intent()
            intent.putExtra("name", BrandName)
            intent.putExtra("qty", Quantity)
            setResult(123, intent)
            finish()




        }



    }


    private fun showToastLong(message: String) {

        Toast.makeText(applicationContext,message, Toast.LENGTH_SHORT).show()

    }




}
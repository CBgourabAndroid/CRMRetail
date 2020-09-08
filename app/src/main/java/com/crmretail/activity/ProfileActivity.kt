package com.crmretail.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.crmretail.R
import com.crmretail.modelClass.AreaInfo
import com.crmretail.modelClass.PersonalInfo
import com.crmretail.shared.UserShared
import com.crmretail.utils.Utility
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.contain_profile.*
import java.io.*
import java.util.*

class ProfileActivity : AppCompatActivity(){

    lateinit var psh : UserShared
    lateinit var userName:TextView
    lateinit var userNumber:TextView
    lateinit var userAddress:TextView
    lateinit var userEmail:TextView
    lateinit var userDlrCode:TextView
    lateinit var userImg:ImageView

    lateinit var datalist: ArrayList<PersonalInfo>

    private var userChoosenTask: String? = null
    private val REQUEST_CAMERA = 0
    private val SELECT_FILE = 1
    private val TAG: String? = null
    private var picturepath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.profile_activity)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {


            finish()
        }
        psh= UserShared(this)



        init()
        onclick()
        //setdata()

        loadData()

        setdata()



    }


    fun loadData(){

        val gson = Gson()
        val json =psh.personalList
        val turnsType = object : TypeToken<ArrayList<PersonalInfo>>() {}.type
        datalist=gson.fromJson(json,turnsType)


    }


    private fun init() {


        userName=findViewById(R.id.tvName)
        userNumber=findViewById(R.id.tvNumber)
        userAddress=findViewById(R.id.tvAddress)
        userEmail=findViewById(R.id.tvEmail)
        userDlrCode=findViewById(R.id.tvDlrCode)
        userImg=findViewById(R.id.profile_img)
    }

    private fun setdata() {

       /* Glide.with(this)
            .load(psh.userPic)
            .apply(RequestOptions().circleCrop())
            .into(profile_image)*/

        userName.text=datalist[0].name
        userNumber.text=datalist[0].contact
        userAddress.text=datalist[0].spDesignation
        tvEmail.text=datalist[0].reporting
        tvDlrCode.text=datalist[0].area
        tvdoj.text=datalist[0].doj

        Glide.with(this)
            .load(R.drawable.ss)
            .apply(RequestOptions().circleCrop())
            .into(userImg);
        //userEmail.text=psh.email
        //userDlrCode.text=psh.dlrcod
    }



    private fun onclick() {
        userImg.setOnClickListener {
            selectImage()
        }

    }


    override fun onBackPressed() {

        finish()
    }



    private fun selectImage() {

        val items = arrayOf<CharSequence>(
            resources.getString(R.string.takephoto),
            resources.getString(R.string.choosefromlib),
            resources.getString(R.string.cancel)
        )

        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.addphoto))
        builder.setItems(items) { dialog, item ->
            val result = Utility.checkPermission(this)

            if (items[item] == resources.getString(R.string.takephoto)) {
                userChoosenTask = resources.getString(R.string.takephoto)
                if (result)
                    cameraIntent()

            } else if (items[item] == resources.getString(R.string.choosefromlib)) {
                userChoosenTask = resources.getString(R.string.choosefromlib)
                if (result)
                    galleryIntent()

            } else if (items[item] == resources.getString(R.string.cancel)) {
                dialog.dismiss()
            }
        }
        builder.show()


    }

    private fun galleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE)


    }

    private fun cameraIntent() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (userChoosenTask == getString(R.string.takephoto))
                    cameraIntent()
                else if (userChoosenTask == getString(R.string.choosefromlib))
                    galleryIntent()
            } else {
                //code for deny
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == SELECT_FILE || requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
                if (data.data != null) {
                    picturepath = data.data


                } else {

                    val photo = data.extras!!.get("data") as Bitmap?
                    picturepath = getImageUri(this, photo!!)
                }

                if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {

                    onSelectFromGalleryResult(data)


                } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
                    onCaptureImageResult(data)

                }


            } else if (resultCode == Activity.RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(
                    this,
                    getString(R.string.Usercancelledimagecapture), Toast.LENGTH_SHORT
                )
                    .show()
            }


            else {

                // failed to capture image
                Toast.makeText(
                    this,
                    getString(R.string.SorryFailedtocaptureimage), Toast.LENGTH_SHORT
                )
                    .show()

            }
        }



        else {
            Toast.makeText(
                this,
                getString(R.string.Usercancelledimagecapture), Toast.LENGTH_SHORT
            )
                .show()
        }


    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun onCaptureImageResult(data: Intent) {
        val thumbnail = data.extras!!.get("data") as Bitmap?
        val bytes = ByteArrayOutputStream()
        //thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)

        val destination = File(
            Environment.getExternalStorageDirectory(),
            System.currentTimeMillis().toString() + ".jpg"
        )

        val fo: FileOutputStream
        try {
            destination.createNewFile()
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // profile_image.setImageBitmap(thumbnail)
        // this.profileImage.setRotation(90);

        /*Glide.with(this)
            .asBitmap()
            .load(thumbnail)
            .apply(RequestOptions().circleCrop())
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Toast.makeText(applicationContext,picturepath.toString(),Toast.LENGTH_LONG).show()
                    profile_image.setImageBitmap(resource)
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    // this is called when imageView is cleared on lifecycle call or for
                    // some other reason.
                    // if you are referencing the bitmap somewhere else too other than this imageView
                    // clear it here as you can no longer have the bitmap
                }
            })*/

        if (!TextUtils.isEmpty(picturepath.toString())) {
            Glide.with(this)
                .load(picturepath)
                .apply(RequestOptions().circleCrop())
                .into(userImg)



        }


    }

    private fun onSelectFromGalleryResult(data: Intent?) {

        if (!TextUtils.isEmpty(picturepath.toString())) {
            Glide.with(this)
                .load(picturepath)
                .apply(RequestOptions().circleCrop())
                .into(userImg)


        }
    }
}
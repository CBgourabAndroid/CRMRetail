package com.crmretail.activity

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.viewpager.widget.ViewPager
import com.crmretail.PostInterface
import com.crmretail.R
import com.crmretail.adapter.BrandTAdapter
import com.crmretail.adapter.MyAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.contain_brand_track.*
import kotlinx.android.synthetic.main.contain_new_register.*
import java.io.File

class BrandTrackActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var tablayout: TabLayout

    var viewPager: ViewPager? = null

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf<String>(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }
        setContentView(R.layout.brand_track_main)
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)



        toolbar.setNavigationOnClickListener {

            onBackPressed()
        }

        verifyStoragePermissions(this)
        inti()

    }

    private fun inti() {

        tablayout=findViewById(R.id.tab_layout)
        viewPager = findViewById<ViewPager>(R.id.viewPager)


        tablayout!!.addTab(tablayout!!.newTab().setText("Recommendation"))
        tablayout!!.addTab(tablayout!!.newTab().setText("Media Control"))

        tablayout.getTabAt(0)!!.select()
        tablayout.tabMode = TabLayout.MODE_AUTO

        tablayout.setTabTextColors(
            ContextCompat.getColorStateList(this,R.color.white)
        )
        tablayout.setSelectedTabIndicatorColor(resources.getColor(R.color.white))
        tablayout.setTabTextColors(resources.getColor(R.color.white),resources.getColor(R.color.grey)
        )


        val adapter = BrandTAdapter(this, supportFragmentManager, tablayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tablayout))

        tablayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    fun verifyStoragePermissions(activity: Activity) {
        // Check if we have write permission
        val permission: Int = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
        else{
            // Toast.makeText(this,"Permission Required",Toast.LENGTH_SHORT).show()
        }
    }




}
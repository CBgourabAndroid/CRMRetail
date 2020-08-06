package com.crmretail.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.crmretail.fragment.*

class BrandTAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                return RecomentadionFragment()
            }
            1 -> {
                return MediaControlFragment()
            }

            else -> return null!!
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}
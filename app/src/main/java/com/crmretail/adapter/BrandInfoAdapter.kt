package com.crmretail.adapter

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.recyclerview.widget.RecyclerView
import com.crmretail.R
import com.crmretail.fragment.SellingBrandAdapter
import com.crmretail.modelClass.BrandInfo
import com.crmretail.modelClass.SelleingBrand

class BrandInfoAdapter (val context: Context) : RecyclerView.Adapter<BrandInfoAdapter.MyViewHolder>() {

    var dataList : ArrayList<BrandInfo>?=null
    lateinit var progressDialog: ProgressDialog


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_brand_info,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        progressDialog= ProgressDialog(context)
        holder.tv_name.setText(dataList!![position].currentBrand)
        holder.tv_speciality.setText(dataList!![position].yearlyLifting+"/PM")





    }




    fun setDataListItems(
        historyList: ArrayList<BrandInfo>
    ){
        this.dataList = historyList

        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val tv_name: TextView = itemView!!.findViewById(R.id.textFishName)
        val tv_speciality: TextView = itemView!!.findViewById(R.id.textViewname)

    }



}
package com.crmretail.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crmretail.PostInterface
import com.crmretail.R
import com.crmretail.modelClass.Holiday
import com.crmretail.modelClass.LeaveList

class LeaveListAdapter  : RecyclerView.Adapter<LeaveListAdapter.MyViewHolder>(){

    lateinit var context: Context
    lateinit var dataList: ArrayList<LeaveList>

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal var lbl_StatusColor: TextView
        internal var lbl_textViewStatus: TextView
        internal var lbl_textViewtime: TextView
        internal var lbl_textViewReason: TextView


        init {

            lbl_StatusColor = itemView.findViewById(R.id.stats_color)
            lbl_textViewStatus = itemView.findViewById(R.id.textViewStatus)
            lbl_textViewtime = itemView.findViewById(R.id.textViewtime)
            lbl_textViewReason = itemView.findViewById(R.id.textViewReason)



        }
    }

    fun setHistoryListItems(
        context: Context,
        dataList: ArrayList<LeaveList>

    ){
        this.dataList = dataList
        this.context=context
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {


        holder.lbl_textViewStatus.setText("Status : "+dataList[listPosition].leaveStatus.toString())
        holder.lbl_textViewtime.setText("Time Frame :  "+dataList[listPosition].leaveStatus.toString())
        holder.lbl_textViewReason.setText("Reason of Leave\n"+dataList[listPosition].leaveStatus.toString())

        if (dataList[listPosition].leaveStatus.equals("Not Approved")){

            holder.lbl_StatusColor.setBackgroundColor(Color.parseColor("#EBF11707"));
        }
        else if (dataList[listPosition].leaveStatus.equals("Approved")){

            holder.lbl_StatusColor.setBackgroundColor(Color.parseColor("#1A951F"));
        }
        else{

            holder.lbl_StatusColor.setBackgroundColor(Color.parseColor("#F36F21"));
        }





    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeaveListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_holyday, parent, false)


        return LeaveListAdapter.MyViewHolder(view)
    }



    override fun getItemCount(): Int {
        return dataList.size
    }


    // Filter Class
    /*fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        originalData!!.clear()
        if (charText.length == 0) {
            originalData!!.addAll(filteredData!!)
        } else {
            for (wp in filteredData!!) {
                if (wp.specialityname.toLowerCase(Locale.getDefault()).contains(charText)) {
                    originalData!!.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }*/


}
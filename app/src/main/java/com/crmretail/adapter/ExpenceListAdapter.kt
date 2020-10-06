package com.crmretail.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crmretail.R
import com.crmretail.modelClass.Expense
import com.crmretail.modelClass.LeaveList

class ExpenceListAdapter : RecyclerView.Adapter<ExpenceListAdapter.MyViewHolder>(){

    lateinit var context: Context
    lateinit var dataList: ArrayList<Expense>

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        //internal var lStatusColor: TextView
        internal var lbl_textViewtrwith: TextView
        internal var lbl_textViewdate: TextView
        internal var lbl_textViewAmt: TextView
        internal var lbl_textViewstartkm: TextView
        internal var lbl_textViewendkm: TextView


        init {

           // lStatusColor = itemView.findViewById(R.id.stats_color)
            lbl_textViewtrwith = itemView.findViewById(R.id.textViewtrwith)
            lbl_textViewdate = itemView.findViewById(R.id.textViewdate)
            lbl_textViewAmt = itemView.findViewById(R.id.textViewAmt)
            lbl_textViewstartkm = itemView.findViewById(R.id.textViewstartkm)
            lbl_textViewendkm = itemView.findViewById(R.id.textViewendkm)



        }
    }

    fun setHistoryListItems(
        context: Context,
        dataList: ArrayList<Expense>

    ){
        this.dataList = dataList
        this.context=context
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {


        if (dataList[listPosition].expFor.contains("Fooding")&&dataList[listPosition].expFor.contains("Others")){
            holder.lbl_textViewstartkm.visibility=View.GONE
            holder.lbl_textViewendkm.visibility=View.GONE


        }



        if (dataList[listPosition].expFor!=null&&dataList[listPosition].expensesFor!=null){
            holder.lbl_textViewtrwith.setText(dataList[listPosition].expFor+" : "+dataList[listPosition].expensesFor)

        }

        if (dataList[listPosition].expDate!=null){
            holder.lbl_textViewdate.setText("Date :  "+dataList[listPosition].expDate.toString())

        }

        if (dataList[listPosition].amount!=null){
            holder.lbl_textViewAmt.setText("Amount : ₹"+dataList[listPosition].amount.toString())

        }

        if (dataList[listPosition].stKm!=null&&dataList[listPosition].endKm!=null){
            holder.lbl_textViewstartkm.setText("Starting KM :  "+dataList[listPosition].stKm.toString()+"Km  /  End KM : "+dataList[listPosition].endKm+"km")


        }

        if (dataList[listPosition].stFrom!=null&&dataList[listPosition].endTo!=null){
            holder.lbl_textViewendkm.setText("From : "+dataList[listPosition].stFrom.toString()+" / To : "+dataList[listPosition].endTo)


        }






        /*if (dataList[listPosition].leaveStatus.equals("Not Approved")){

            holder.lStatusColor.setBackgroundColor(Color.parseColor("#EBF11707"));
        }
        else if (dataList[listPosition].leaveStatus.equals("Approved")){

            holder.lStatusColor.setBackgroundColor(Color.parseColor("#1A951F"));
        }
        else{

            holder.lStatusColor.setBackgroundColor(Color.parseColor("#F36F21"));
        }*/





    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpenceListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expence_list, parent, false)


        return ExpenceListAdapter.MyViewHolder(view)
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
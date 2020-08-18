package com.crmretail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.crmretail.PostInterface
import com.crmretail.R
import com.crmretail.modelClass.EventList

class EventListAdapter  : RecyclerView.Adapter<EventListAdapter.MyViewHolder>(){

    lateinit var context: Context
    lateinit var dataList: ArrayList<EventList>

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        internal var lbl_textViewType: TextView
        internal var lbl_textViewtime: TextView
        internal var lbl_textViewloc: TextView

        internal var lbl_firstD: TextView
        internal var lbl_monthD: TextView


        init {

            lbl_textViewType = itemView.findViewById(R.id.textViewType)
            lbl_textViewtime = itemView.findViewById(R.id.textViewtime)
            lbl_textViewloc = itemView.findViewById(R.id.textViewloc)
            lbl_firstD=itemView.findViewById(R.id.firstD)
            lbl_monthD=itemView.findViewById(R.id.monthD)



        }
    }

    fun setHistoryListItems(
        context: Context,
        dataList: ArrayList<EventList>

    ){
        this.dataList = dataList
        this.context=context
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {

        holder.lbl_textViewType.setText(dataList[listPosition].eventType.toString())
        holder.lbl_textViewtime.setText("Timming : "+dataList[listPosition].startTime.toString()+"-"+dataList[listPosition].timeFormat.toString())
        holder.lbl_textViewloc.setText(dataList[listPosition].location.toString())


        holder.lbl_monthD.setText(PostInterface.format_edatem(dataList[listPosition].eventDate))
        holder.lbl_firstD.setText(PostInterface.format_edated(dataList[listPosition].eventDate))





    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event_list, parent, false)


        return EventListAdapter.MyViewHolder(view)
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
package com.crmretail.adapter

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.recyclerview.widget.RecyclerView
import com.crmretail.R
import com.crmretail.modelClass.PrivateTravelModel

class PrivateDataAdapter (val context: Context) : RecyclerView.Adapter<PrivateDataAdapter.MyViewHolder>() {

    var dataList : ArrayList<PrivateTravelModel>?=null
    lateinit var progressDialog: ProgressDialog

    lateinit  var addFoodTXt:TextView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_list,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        progressDialog= ProgressDialog(context)


        holder.tv_name.setText(dataList!![position].travelwith)
        holder.tv_speciality.setText("Meter Reading : "+dataList!![position].privateTransKm+"km")
        val sss= dataList!![position].sopportPic
        //val sss= dataList!![position].sopportPic
      //  val myUri: Uri = Uri.parse(dataList!![position].sopportPic)
       // holder.img_cat.setImageURI(myUri)
        if (!sss.equals("")){
            val takenImage = BitmapFactory.decodeFile(dataList!![position].sopportPic.absolutePath)
            holder.img_cat.setImageBitmap(takenImage)

        }
        else{
            holder.img_cat.setImageResource(R.drawable.ic_launcher_background)

        }
      /*  if (!sss.equals("null")){


        }
        else{
            holder.img_cat.setImageResource(R.drawable.ic_launcher_background)

        }*/


        //        holder.img_cat.setImageResource(currResource)
        /*    Glide.with(context).load(dataList!![position].image)
                .apply(RequestOptions().circleCrop())
                //.apply(RequestOptions().override(100,100))
                .into(holder.img_cat)*/

        holder.itemView.setOnClickListener {

/*
            val detailIntent = Intent(context, EditSpecialistActivity::class.java)
            detailIntent.putExtra("id",dataList!![position].splId)
            detailIntent.putExtra("name",dataList!![position].name)
            detailIntent.putExtra("image",dataList!![position].image)
            detailIntent.putExtra("designation",dataList!![position].designation)
            detailIntent.putExtra("catid",dataList!![position].catId)
            detailIntent.putExtra("subcatid",dataList!![position].subcatId)
            detailIntent.putExtra("category_name",dataList!![position].categoryName)
            detailIntent.putExtra("sub_category_name",dataList!![position].subCategoryName)
            context.startActivity(detailIntent)*/
            menuFunction(holder.menuImg, position)



        }

        /* holder.menuImg.setOnClickListener {

             menuFunction(holder.menuImg, position)
         }*/


    }




    fun setDataListItems(
        historyList: ArrayList<PrivateTravelModel>,
        addFood: TextView
    ){
        this.dataList = historyList
        this.addFoodTXt=addFood

        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val tv_name: TextView = itemView!!.findViewById(R.id.textFishName)
        val tv_speciality: TextView = itemView!!.findViewById(R.id.textViewname)
        val img_cat: ImageView = itemView!!.findViewById(R.id.ivFish)
        val menuImg: ImageView =itemView!!.findViewById(R.id.img_menu)



    }

    @SuppressLint("RestrictedApi")
    private fun menuFunction(menuImg: ImageView, listPosition: Int) {


        val menuBuilder = MenuBuilder(context)
        val inflater = MenuInflater(context)
        inflater.inflate(R.menu.menu_delete, menuBuilder)

        @SuppressLint("RestrictedApi")
        val optionsMenu = MenuPopupHelper(context, menuBuilder, menuImg)
        optionsMenu.setForceShowIcon(true)


        // Set Item Click Listener
        menuBuilder.setCallback(object : MenuBuilder.Callback {
            override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.menu3 // Handle option1 Click
                    -> {
                        optionsMenu.dismiss()

                        dataList!!.removeAt(listPosition)
                        addFoodTXt.visibility=View.VISIBLE
                        notifyDataSetChanged()


                        return true
                    }



                    else -> return false
                }
            }

            override fun onMenuModeChange(menu: MenuBuilder) {}
        })


// Display the menu
        optionsMenu.show()
    }


}
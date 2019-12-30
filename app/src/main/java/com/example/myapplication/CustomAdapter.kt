package com.example.myapplication

import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class CustomAdapter(private val mList: ArrayList<MainActivity.ContactItem>?) :
    RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView
        var phNumber: TextView


        init {
            this.name = view.findViewById(R.id.name_listitem) as TextView
            this.phNumber = view.findViewById(R.id.phNumber_listitem) as TextView
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CustomViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_list, viewGroup, false)

        return CustomViewHolder(view)
    }


    override fun onBindViewHolder(viewholder: CustomViewHolder, position: Int) {

        viewholder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f)
        viewholder.phNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f)

        viewholder.name.gravity = Gravity.CENTER
        viewholder.phNumber.gravity = Gravity.CENTER



        viewholder.name.setText(mList!![position].user_Name)
        viewholder.phNumber.setText(mList[position].user_phNumber)
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

}

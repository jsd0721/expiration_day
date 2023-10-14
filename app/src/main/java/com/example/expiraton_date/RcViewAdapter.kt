package com.example.expiraton_date

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expiraton_date.databinding.ItemlayoutBinding
import com.example.expiraton_date.domain.ProductItemInRecyclerViewDTO

class RcViewAdapter(context: Context, list:MutableList<ProductItemInRecyclerViewDTO>) : RecyclerView.Adapter<RcViewAdapter.MyViewHolder>() {

    /*binding*/
    private val itemList : MutableList<ProductItemInRecyclerViewDTO> = list
    private val adapterContext = context
    class MyViewHolder(binding: ItemlayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        val productNameTextView : TextView = binding.prNameTextView
        val expirationDateTextView : TextView = binding.expirationDateTextView
        val categoryTextView : TextView = binding.categoryTextView
        val savedPlaceTextView : TextView = binding.savedPlaceTextView
        val remainDateTextView : TextView = binding.remainDateTextView
        val iconImageView : ImageView = binding.itemIconImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayoutBinding = ItemlayoutBinding.inflate(LayoutInflater.from(adapterContext),parent,false)
        return MyViewHolder(itemLayoutBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val remainDate = itemList[position].remainDate.toString()

        holder.productNameTextView.text = itemList[position].productName
        holder.expirationDateTextView.text = itemList[position].expirationDate
        holder.categoryTextView.text = itemList[position].category
        holder.savedPlaceTextView.text = itemList[position].savedPlace
        holder.remainDateTextView.text = remainDate + "일"
        holder.remainDateTextView.setTextColor(setTextColorInRecyclerView(remainDate))
        holder.iconImageView.imageAlpha = itemList[position].image
    }

    fun setTextColorInRecyclerView(day : String) : Int{
        return when(day.toInt()){
            in 0..3-> Color.parseColor("#D80032")
            in 4..7-> Color.parseColor("#E9B824")
            else-> Color.parseColor("#004225")
        }
    }

}

package com.example.expiraton_date

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expiraton_date.databinding.ItemlayoutBinding

class RcViewAdapter(context: Context, list:MutableList<ProductItemDTO>) : RecyclerView.Adapter<RcViewAdapter.MyViewHolder>() {

    /*binding*/
    private val itemList : MutableList<ProductItemDTO> = list
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
        holder.productNameTextView.text = itemList[position].productName
        holder.expirationDateTextView.text = itemList[position].expirationDate
        holder.categoryTextView.text = itemList[position].category
        holder.savedPlaceTextView.text = itemList[position].savedPlace
        holder.remainDateTextView.text = itemList[position].remainDate.toString()
        holder.iconImageView.imageAlpha = itemList[position].image
    }

}

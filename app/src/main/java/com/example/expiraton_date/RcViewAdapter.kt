package com.example.expiraton_date

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.expiraton_date.databinding.ItemlayoutBinding
import com.example.expiraton_date.domain.ProductItemInRecyclerViewDTO
import com.example.expiraton_date.sqliteRoom.ProductDatabase

class RcViewAdapter(context: Context, list:MutableList<ProductItemInRecyclerViewDTO>, modifyActive : Boolean) : RecyclerView.Adapter<RcViewAdapter.MyViewHolder>() {

    /*binding*/
    private val itemList : MutableList<ProductItemInRecyclerViewDTO> = list
    private val adapterContext = context
    private val modifyActivityActiveValue = modifyActive

    class MyViewHolder(binding: ItemlayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        val productNameTextView : TextView = binding.prNameTextView
        val expirationDateTextView : TextView = binding.expirationDateTextView
        val categoryTextView : TextView = binding.categoryTextView
        val savedPlaceTextView : TextView = binding.savedPlaceTextView
        val remainDateTextView : TextView = binding.remainDateTextView
        val iconImageView : ImageView = binding.itemIconImageView
    }
    /*viewholder가 만들어질 때 viewbinding이 이루어지고, 해당 view를 MyViewHolder에 담아서 리턴.*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayoutBinding = ItemlayoutBinding.inflate(LayoutInflater.from(adapterContext),parent,false)
        return MyViewHolder(itemLayoutBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    /*리사이클러뷰에 아이템을 바인딩하는 부분. 해당 부분에서 */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val remainDate = itemList[position].remainDate.toString()

        if(modifyActivityActiveValue){
            holder.itemView.setOnClickListener(View.OnClickListener {
                val intent = Intent(it.context,ModifyActivity::class.java)
                intent.putExtra("productNameExtra",itemList[position].productName)
                adapterContext.startActivity(intent)
            })
        }

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
            in 7..1000-> Color.parseColor("#186F65")
            in 4..7-> Color.parseColor("#E9B824")
            else-> Color.parseColor("#D80032")
        }
    }

}

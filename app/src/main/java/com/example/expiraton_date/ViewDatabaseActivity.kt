package com.example.expiraton_date

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expiraton_date.databinding.ActivityViewDatabaseBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.expiraton_date.domain.ProductItemInRecyclerViewDTO
import com.example.expiraton_date.sqliteRoom.ProductDatabase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class ViewDatabaseActivity : AppCompatActivity(),OnClickListener {

    /*components*/
    private lateinit var itemRCView : RecyclerView
    private lateinit var registerButton : FloatingActionButton

    private var backPressedTime : Long = 0L

    private val binding : ActivityViewDatabaseBinding by lazy{
        ActivityViewDatabaseBinding.inflate(layoutInflater)
    }

    /*ItemList in RecyclerView*/
    private var itemList : MutableList<ProductItemInRecyclerViewDTO> = mutableListOf()

    /*recyclerView parts*/
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var rcViewAdapter :RcViewAdapter

    private val database by lazy{
        ProductDatabase.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindView()
    }

    override fun onStart() {
        super.onStart()

    }

    private fun bindView() {
        //component binding
        registerButton = binding.regButton
        itemRCView = binding.itemRCView

        //inquire and bind data with recyclerview
        Thread{
            inquireAllItem()
            runOnUiThread{
                layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
                rcViewAdapter = RcViewAdapter(this,itemList)

                itemRCView.adapter = rcViewAdapter
                itemRCView.layoutManager = layoutManager
            }
        }


        registerButton.setOnClickListener(this)


    }

    private fun inquireAllItem(){
        val inquiredProducts = database!!.productTableDao().getAllItem()

        if(itemList.size>0){
            itemList.clear()
        }

        for(elem in inquiredProducts){
            val currentDate = LocalDate.now().toString()
            val expirationDate = elem.productExpirationDate

            val remainDate = calculateRemainDate(SimpleDateFormat("yyyy-mm-dd").parse(currentDate),SimpleDateFormat("yyyy-mm-dd").parse(expirationDate))

            val tempDTO = ProductItemInRecyclerViewDTO(
                    elem.productImage,
                    elem.productName,
                    elem.productExpirationDate,
                    elem.savePlace,
                    elem.category,
                    remainDate
            )
            itemList.add(tempDTO)
        }
    }

    private fun calculateRemainDate(startDate : Date, endDate : Date) : Int {
        val startDateTime : Long = startDate.time
        val endDateTime : Long = endDate.time

        val result = endDateTime-startDateTime
        return when(result%86400*1000){
            0L -> (result/86400*1000).toInt()
            else-> (result/86400*1000+1).toInt()
        }
    }

    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime
        if (intervalTime in 0..2000) {
            super.onBackPressed()
        } else {
            backPressedTime = tempTime
            Toast.makeText(applicationContext, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onClick(view: View?) {
        lateinit var intent : Intent
        when(view){
            registerButton->intent = Intent(this, RegisterChoiceActivity::class.java)
        }
        startActivity(intent)
    }
}
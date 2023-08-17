package com.example.expiraton_date

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expiraton_date.databinding.ActivityViewDatabaseBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ViewDatabaseActivity : AppCompatActivity(),OnClickListener {

    /*components*/
    private lateinit var itemRCView : RecyclerView
    private lateinit var registerButton : FloatingActionButton

    private var backPressedTime : Long = 0L

    private val binding : ActivityViewDatabaseBinding by lazy{
        ActivityViewDatabaseBinding.inflate(layoutInflater)
    }

    /*ItemList in RecyclerView*/
    private val itemList : MutableList<ProductItemDTO> = mutableListOf()

    /*recyclerView parts*/
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var rcViewAdapter :RcViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rcViewAdapter = RcViewAdapter(this,itemList)

        bindView()
    }

    private fun bindView() {

        registerButton = binding.regButton
        itemRCView = binding.itemRCView

        registerButton.setOnClickListener(this)

        itemRCView.adapter = rcViewAdapter
        itemRCView.layoutManager = layoutManager

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
            registerButton->intent = Intent(this, RegisterChoiseActivity::class.java)
        }
        startActivity(intent)
    }
}
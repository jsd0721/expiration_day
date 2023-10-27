package com.example.expiraton_date

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expiraton_date.databinding.ActivityViewDatabaseBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.expiraton_date.domain.ProductItemInRecyclerViewDTO
import com.example.expiraton_date.sqliteRoom.ProductDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class ViewDatabaseActivity : AppCompatActivity(),OnClickListener {

    /*components*/
    private lateinit var itemRCView : RecyclerView
    private lateinit var registerButton : FloatingActionButton
    private lateinit var imageWhenEmpty : ImageView
    private lateinit var toolbar : Toolbar

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

        CoroutineScope(Dispatchers.IO).launch{
            inquireAllItem()
            CoroutineScope(Dispatchers.Main).launch{
                viewChange()
            }
        }
    }

    private fun bindView() {
        //component binding
        registerButton = binding.regButton
        itemRCView = binding.itemRCView
        imageWhenEmpty = binding.imageWhenEmpty
        toolbar = binding.mainActivityToolbar

        setSupportActionBar(toolbar)

        layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rcViewAdapter = RcViewAdapter(applicationContext,itemList,true)

        itemRCView.adapter = rcViewAdapter
        itemRCView.layoutManager = layoutManager

        registerButton.setOnClickListener(this)
    }

    private fun viewChange() {
        when (itemList.size) {
            0 -> {
                itemRCView.visibility = View.GONE
                imageWhenEmpty.visibility = View.VISIBLE
            }

            else -> {
                itemRCView.visibility = View.VISIBLE
                imageWhenEmpty.visibility = View.GONE
            }
        }

        rcViewAdapter.notifyDataSetChanged()
    }

    private fun inquireAllItem(){

//      1.데이터를 데이터베이스에서 갸져옴
        val inquiredProducts = database!!.productTableDao().getAllItem()

//      2.리사이클러뷰에 넘겨줄 데이터 리스트가 비어있지 않다면 비워줌(새로 데이터를 채워서 갱신시키기 위함)
        if(itemList.size>0){
            itemList.clear()
        }

//      3.조회한 데이터들을 리스트에 추가함
        for(elem in inquiredProducts) {
            val currentDate = LocalDate.now().toString()
            val expirationDate = elem.productExpirationDate

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

            Log.d("calculatedDateTag","startDate : $currentDate, endDate : $expirationDate")
            Log.d("afterDataFormated","currentDate : ${SimpleDateFormat("yyyy-MM-dd",).parse(currentDate)},expirationDate : ${SimpleDateFormat("yyyy-MM-dd").parse(expirationDate)}")

            val remainDate = calculateRemainDate(simpleDateFormat.parse(currentDate),simpleDateFormat.parse(expirationDate))

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

        Log.d("resultValue","result : ${result / (86400 * 1000)}")
        return (result / (86400 * 1000)).toInt()
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

//    툴바 메뉴 생성시 작동
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_main_activity_toopbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.calendar->{
                //TODO Intent CalendarActivity with itemList
            }
            R.id.search->{
                intent = Intent(this@ViewDatabaseActivity,Inquire::class.java)
                startActivity(intent)
            }
            R.id.setting->{

            }

        }
        return true
    }
}
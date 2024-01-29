package com.example.expiraton_date

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.expiraton_date.databinding.ActivityCalendarBinding
import com.example.expiraton_date.sqliteRoom.ProductDatabase
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.coroutines.runBlocking
import java.util.Calendar

class CalendarActivity : AppCompatActivity(), OnDateSelectedListener {

    private val calendarActivityBinding by lazy{
        ActivityCalendarBinding.inflate(layoutInflater)
    }

    private val db = ProductDatabase.getInstance(this)
    private val currentDate = Calendar.getInstance()

    private lateinit var itemInSelectedDateRCView : RecyclerView
    private lateinit var materialCalendarView : MaterialCalendarView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(calendarActivityBinding.root)

        bindview()
    }

    private fun bindview() {

        materialCalendarView = calendarActivityBinding.calendarView
        materialCalendarView.setDateSelected(currentDate,true)
        materialCalendarView.setOnDateChangedListener(this)
    }

    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
        val itemList = getProductList(currentDate)

    }
    private fun getProductList(date : Calendar) : MutableList<HashMap<String,String>>{
        val result = mutableListOf<HashMap<String,String>>()
        runBlocking {
            val data = db?.productTableDao()?.getItemWithExpirationDate("${currentDate.get(Calendar.YEAR)}-${currentDate.get(Calendar.MONTH)}-${currentDate.get(Calendar.DATE)}")
            if (data != null) {
                for(elem in data){
                    val resultListItem = HashMap<String, String>()
                    resultListItem.put("name",elem.productName)
                    resultListItem.put("ExpirationDate",elem.productExpirationDate)
                }
            }
        }
        return result
    }
}
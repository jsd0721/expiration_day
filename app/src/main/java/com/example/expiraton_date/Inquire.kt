package com.example.expiraton_date

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expiraton_date.databinding.ActivityInquireBinding
import com.example.expiraton_date.domain.ProductItemInRecyclerViewDTO
import com.example.expiraton_date.sqliteRoom.ProductDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class Inquire : AppCompatActivity(),OnClickListener {

    /*1. onStart Callback Method 에서  tempItemList 에 모든 item List를 DB에서 불러옴
    * 2. 불러온 tempList에 담긴 모든 데이터들을 itemList에 복사해 담고, RecyclerView어댑터 생성자에 itemList 를 파라미터로 전달해 객체 생성
    * 3. 그 후 LayoutManager 와 RecyclerViewAdapter 를 recyclerView에 부착해 모든 리스트 표시.(즉, RCView에 표시되는 아이템은 itemlist의 것)
    * 4. 검색 로직 수행시 itemList clear, tempItemList 에서 filter작업을 하여 해당하는 아이템들을 itemList에 삽입 후 Recyclerview에 notify 주기 */

    /*component*/
    private lateinit var findItemRcView : RecyclerView
    private lateinit var categorySpinner: Spinner
    private lateinit var savePlaceSpinner: Spinner
    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var itemNotFoundImage : ImageView

    /*recyclerView 관련*/
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var rcViewAdapter: RcViewAdapter

    /*Get DB Instance*/
    private val db : ProductDatabase? by lazy{
        ProductDatabase.getInstance(this@Inquire)
    }

    /*item List*/
    private val itemList : MutableList<ProductItemInRecyclerViewDTO> by lazy{
        mutableListOf()
    }

    /*All item list*/
    val tempItemList : MutableList<ProductItemInRecyclerViewDTO> = mutableListOf()

    private val binding : ActivityInquireBinding by lazy{
        ActivityInquireBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        categorySpinner = binding.spinnerCategory
        savePlaceSpinner = binding.spinnerSavePlace
        findItemRcView = binding.inquireRcView
        searchButton = binding.searchButton
        searchEditText = binding.editSearch
        itemNotFoundImage = binding.itemNotExistImage

        searchButton.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()

        /*아이템의 중복 표시를 방지하기 위해 액티비티가 시작될때마다 list를 비워줌*/
        if(tempItemList.size>0){
            tempItemList.clear()
        }

        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                val itemInquireResult=db!!.productTableDao().getAllItem()
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                for(elem in itemInquireResult){
                    tempItemList.add(ProductItemInRecyclerViewDTO(
                        elem.productImage,elem.productName,
                        elem.productExpirationDate,
                        elem.savePlace,elem.category,
                        calculateRemainDate(sdf.parse(LocalDate.now().toString()),sdf.parse(elem.productExpirationDate))
                    ))
                }

                layoutManager = LinearLayoutManager(this@Inquire,LinearLayoutManager.VERTICAL,false)
                rcViewAdapter = RcViewAdapter(this@Inquire,itemList,false)

                findItemRcView.adapter = rcViewAdapter
                findItemRcView.layoutManager = layoutManager

                itemList.addAll(tempItemList)

                changeVisibility(itemList)

            }
        }
    }

    private fun changeVisibility(listItem:MutableList<ProductItemInRecyclerViewDTO>){
        if(listItem.size <= 0){
            Log.d("size of ListItem","${listItem.size}")
            itemNotFoundImage.visibility = View.VISIBLE
            findItemRcView.visibility = View.GONE
        }else{
            itemNotFoundImage.visibility = View.GONE
            findItemRcView.visibility = View.VISIBLE
        }
    }


    private fun dialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)

        // 제목셋팅
        alertDialogBuilder.setTitle("알림")

        // AlertDialog 셋팅
        alertDialogBuilder
            .setMessage("분류를 모두 선택해 주세요!")
            .setCancelable(false)
            .setPositiveButton("확인") { dialog, id -> dialog.cancel() }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onClick(view: View?) {
        when(view){
            searchButton->{

                itemList.clear()
                val getCategory = categorySpinner.selectedItem.toString()
                val getSavePlace = savePlaceSpinner.selectedItem.toString()
                val searchedName = searchEditText.text.toString()

                if ((getSavePlace == "" || getCategory == "") && searchedName == "") {
                    dialog()
                } else {
                    itemList.addAll(tempItemList.filter{
                        it.productName == searchedName||(it.category == getCategory && it.savedPlace == getSavePlace)
                    })
                    Log.d("listsize",itemList.size.toString())

                    rcViewAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun calculateRemainDate(startDate : Date, endDate : Date) : Int {

        val startDateTime : Long = startDate.time
        val endDateTime : Long = endDate.time

        val result = endDateTime-startDateTime

        Log.d("resultValue","result : ${result / (86400 * 1000)}")
        return (result / (86400 * 1000)).toInt()
    }
}
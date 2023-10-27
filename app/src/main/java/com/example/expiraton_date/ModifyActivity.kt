package com.example.expiraton_date

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.expiraton_date.databinding.ActivityModifyBinding
import com.example.expiraton_date.sqliteRoom.ProductDatabase
import com.example.expiraton_date.sqliteRoom.ProductTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ModifyActivity : AppCompatActivity(),OnClickListener {

    private val db = ProductDatabase.getInstance(this)

    /*Components*/
    private lateinit var categorySpinner: Spinner
    private lateinit var savePlaceSpinner: Spinner
    private lateinit var modifyBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var productNameEditText: EditText
    private lateinit var yearEditText: EditText
    private lateinit var monthEditText : EditText
    private lateinit var dateEditText : EditText
    private lateinit var itemImageView : ImageView

    /*조회 결과 담을 변수*/
    private lateinit var productInquireResult : ProductTable

    private val binding : ActivityModifyBinding by lazy{
        ActivityModifyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindiView()
    }

    private fun bindiView() {

        //recyclerview에서 받은 intent 오브젝트를 변수로 받음
        val intentFromRcView: Intent = intent
        val productName = intentFromRcView.getStringExtra("productNameExtra")

        //컴포넌트 설정 후 각 컴포넌트에 값 삽입.
        productNameEditText = binding.productNameEditText
        categorySpinner = binding.categorySpinner
        ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item)
            .also{
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                categorySpinner.adapter = it
            }
        savePlaceSpinner = binding.savePlaceSpinner
        ArrayAdapter.createFromResource(this, R.array.saved_place, android.R.layout.simple_spinner_item)
            .also{
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                savePlaceSpinner.adapter = it
            }
        yearEditText = binding.yearEditText
        monthEditText = binding.monthEdiText
        dateEditText = binding.dateEditText

        modifyBtn = binding.buttonMod
        cancelBtn = binding.buttonCancel
        itemImageView = binding.productImageView

        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                productInquireResult = db!!.productTableDao().getItemWithName(productName.toString())

                val year = productInquireResult.productExpirationDate.split("-")[0]
                val month = productInquireResult.productExpirationDate.split("-")[1]
                val date = productInquireResult.productExpirationDate.split("-")[2]

                val categoryArray = resources.getStringArray(R.array.category)
                val savedPlaceArray = resources.getStringArray(R.array.saved_place)

                productNameEditText.setText(productInquireResult.productName)
                yearEditText.setText(year)
                monthEditText.setText(month)
                dateEditText.setText(date)

                categorySpinner.setSelection(selectSpinnerItem(productInquireResult.category,categoryArray))
                savePlaceSpinner.setSelection(selectSpinnerItem(productInquireResult.savePlace,savedPlaceArray))
            }
        }

        modifyBtn.setOnClickListener(this)
        cancelBtn.setOnClickListener(this)
    }

    private fun selectSpinnerItem(item : String, array : Array<String>): Int {
        var position: Int = 0
        for(category in array){
            if(item == category){
                position = array.indexOf(category)
            }else{
                continue
            }
        }
        return position
    }

    override fun onClick(button: View?) {
        when(button){
            modifyBtn->{
                val nameValue = productNameEditText.text.toString()
                val dateValue = "${yearEditText.text}-${monthEditText.text}-${dateEditText.text}"
                val categoryValue = categorySpinner.selectedItem.toString()
                val savePlaceValue = savePlaceSpinner.selectedItem.toString()
                val productImage = itemImageView.imageAlpha

                val resultProductDTO : ProductTable = ProductTable(productInquireResult.id,nameValue,dateValue,categoryValue,savePlaceValue,productImage)

                if ("" == nameValue || yearEditText.text.toString() == "" || monthEditText.text.toString() == "" || dateEditText.text.toString() == "" || "" == categoryValue || "" == savePlaceValue) {
                    Toast.makeText(this@ModifyActivity, "항목을 모두 입력하십시오", Toast.LENGTH_SHORT).show()
                } else {
                    runBlocking {
                        CoroutineScope(Dispatchers.IO).launch{
                            db!!.productTableDao().editProduct(resultProductDTO)
                        }
                    }
                    Toast. makeText(this@ModifyActivity,"변경사항이 저장되었습니다.",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            cancelBtn-> finish()
        }
    }
}
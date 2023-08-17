package com.example.expiraton_date

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.expiraton_date.databinding.ActivityMainBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var myadapter: RcViewAdapter? = null

    //넣을 값들 변수 설정
    private lateinit var productName: EditText
    private lateinit var expirationDate: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var savePlaceSpinner: Spinner
    private lateinit var saveButton: Button

    //viewBinding
    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //xml상에서의 개체들과 각 변수들 연결
        productName = findViewById<View>(R.id.productNameEditText) as EditText
        expirationDate = findViewById<View>(R.id.yearEditText) as EditText
        categorySpinner = findViewById<View>(R.id.categorySpinner) as Spinner
        savePlaceSpinner = findViewById<View>(R.id.savePlaceSpinner) as Spinner
        saveButton = findViewById<View>(R.id.button_save) as Button

        //세이브 버튼 눌렀을때 작동 구현
        saveButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view){
            saveButton->{
                val getName = productName.text.toString()
                val getExDate = expirationDate.text.toString()
                val getCategory = categorySpinner.selectedItem.toString()
                val getSavePlace = savePlaceSpinner.selectedItem.toString()
                if ("" == getName || "" == getExDate || "" == getCategory || "" == getSavePlace) {
                    Toast.makeText(applicationContext, "항목을 모두 입력해주세요", Toast.LENGTH_SHORT).show()
                    return
                }
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")

                var endDate: Date = try {
                    dateFormat.parse(getExDate)
                } catch (e: ParseException) {
                    Toast.makeText(applicationContext, "yyyy-mm-dd형식으로 입력하세요", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }
    }
}